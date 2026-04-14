package addressBook;

import addressBook.entities.*;
import addressBook.repository.*;
import addressBook.service.*;

import com.formdev.flatlaf.FlatLightLaf; 
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuiMain {
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
            UIManager.put("TextComponent.arc", 10);
        } catch (Exception ex) {
            System.err.println("Erreur d'initialisation du thème. Démarrage avec le thème par défaut.");
        }

        // Swing : Lancer l'interface dans le "Event Dispatch Thread"
        SwingUtilities.invokeLater(() -> {
            System.out.println("Démarrage du programme...");

            // --- 1. INITIALISATION DES SERVICES ---
            ContactRepository contactRepo = new JsonContactRepository("contacts.json");
            ContactService contactService = new ContactService(contactRepo);

            InteractionRepository interactionRepo = new MemoryInteractionRepository();
            InteractionService interactionService = new InteractionService(interactionRepo);

            // --- 2. FENÊTRE PRINCIPALE ---
            JFrame fenetre = new JFrame("CRM Groupe 4 - Application Complète");
            fenetre.setSize(900, 700); 
            fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenetre.setLocationRelativeTo(null); // Centre la fenêtre sur l'écran

            JTabbedPane onglets = new JTabbedPane();
            onglets.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            // --- 3. CRÉATION DES ONGLETS ---
            onglets.addTab("👤 Gérer les Contacts", creerOngletContacts(contactService));
            onglets.addTab("📅 Gérer les Interactions", creerOngletInteractions(interactionService));

            // --- 4. ASSEMBLAGE ET AFFICHAGE ---
            fenetre.add(onglets);
            fenetre.setVisible(true);
        });
    }

    // =========================================================================
    // MODULE CONTACTS (Version avec Tableau)
    // =========================================================================
    private static JPanel creerOngletContacts(ContactService service) {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. CRÉATION DU TABLEAU ---
        // On définit les colonnes de notre tableau
        String[] colonnes = {"ID", "Nom", "Email", "Catégories"};
        
        // Le Modèle gère les données. On désactive l'édition manuelle des cases pour éviter les bugs
        javax.swing.table.DefaultTableModel modeleTable = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
        
        JTable tableContacts = new JTable(modeleTable);
        tableContacts.setFillsViewportHeight(true); // Remplit tout l'espace
        tableContacts.setRowHeight(25); // Des lignes un peu plus hautes pour aérer
        tableContacts.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12)); // En-têtes en gras
        
        JScrollPane scrollPane = new JScrollPane(tableContacts);

// --- 2. BARRE D'OUTILS (Recherche, Rafraîchir, Supprimer) ---
        
        // a) La barre de Recherche
        JTextField champRecherche = new JTextField(15);
        champRecherche.putClientProperty("JTextField.placeholderText", "Entrez un nom...");
        JButton btnChercher = new JButton("🔍 Chercher");
        
        btnChercher.addActionListener(e -> {
            String recherche = champRecherche.getText().trim().toLowerCase();
            modeleTable.setRowCount(0); // On vide le tableau
            
            // On récupère les contacts et on les filtre
  
            List<Contact> contacts = service.getAllContacts();
            for (Contact c : contacts) {
                if (recherche.isEmpty() || c.getName().toLowerCase().contains(recherche)) {
                    Object[] ligne = {
                        c.getId(),
                        c.getName(),
                        c.getEmail() != null ? c.getEmail() : "",
                        c.getCategories().toString()
                    };
                    modeleTable.addRow(ligne);
                }
            }
        });

        // b) Le bouton Rafraîchir (qui réinitialise aussi la recherche)
        JButton boutonCharger = new JButton("🔄 Tout afficher");
        boutonCharger.putClientProperty("JButton.buttonType", "default");
        boutonCharger.addActionListener(e -> {
            champRecherche.setText(""); 
            btnChercher.doClick();      
        });

        // c) Le bouton Supprimer
        JButton btnSupprimer = new JButton("🗑️ Supprimer le contact sélectionné");
        btnSupprimer.setBackground(new Color(255, 100, 100)); // Petit fond rouge
        btnSupprimer.setForeground(Color.WHITE);
        btnSupprimer.addActionListener(e -> {
            int selectedRow = tableContacts.getSelectedRow(); 
            
            if (selectedRow >= 0) {
                int confirmation = JOptionPane.showConfirmDialog(
                    null, 
                    "Voulez-vous vraiment supprimer ce contact ?", 
                    "Confirmation de suppression", 
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );

                if (confirmation == JOptionPane.YES_OPTION) {
                    Long contactId = (Long) tableContacts.getValueAt(selectedRow, 0);
                    service.deleteContact(contactId);
                    JOptionPane.showMessageDialog(null, "Le contact a été supprimé avec succès.");
                    boutonCharger.doClick(); // Rafraîchit le tableau
                }
            } else {
                JOptionPane.showMessageDialog(null, "Veuillez d'abord sélectionner un contact dans la liste.", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        // --- ASSEMBLAGE DU PANNEAU HAUT ---
        JPanel panneauHaut = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panneauHaut.add(new JLabel("Rechercher :"));
        panneauHaut.add(champRecherche);
        panneauHaut.add(btnChercher);
        panneauHaut.add(new JLabel("  |  ")); // Petit séparateur visuel
        panneauHaut.add(boutonCharger);
        panneauHaut.add(btnSupprimer);

        // --- 3. FORMULAIRE D'AJOUT ---
        JPanel formulaire = new JPanel(new GridLayout(4, 1, 10, 10));
        formulaire.setBorder(BorderFactory.createTitledBorder("➕ Ajouter un contact"));

        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JTextField champId = new JTextField(5);
        JTextField champNom = new JTextField(15);
        ligne1.add(new JLabel("ID:")); ligne1.add(champId);
        ligne1.add(new JLabel("Nom:")); ligne1.add(champNom);

        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JTextField champEmail = new JTextField(15);
        JTextField champTel = new JTextField(10);
        ligne2.add(new JLabel("Email:")); ligne2.add(champEmail);
        ligne2.add(new JLabel("Tél:")); ligne2.add(champTel);

        JPanel ligne3 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JCheckBox chkClient = new JCheckBox("CLIENT");
        JCheckBox chkProspect = new JCheckBox("PROSPECT");
        JCheckBox chkAmi = new JCheckBox("AMI");
        JCheckBox chkFamille = new JCheckBox("FAMILLE");
        ligne3.add(new JLabel("Catégories: "));
        ligne3.add(chkClient); ligne3.add(chkProspect); ligne3.add(chkAmi); ligne3.add(chkFamille);

        JButton boutonAjouter = new JButton("✅ Enregistrer le contact");
        boutonAjouter.addActionListener(e -> {
            try {
                Set<Category> categories = new HashSet<>();
                if (chkClient.isSelected()) categories.add(Category.CLIENT);
                if (chkProspect.isSelected()) categories.add(Category.PROSPECT);
                if (chkAmi.isSelected()) categories.add(Category.AMI);
                if (chkFamille.isSelected()) categories.add(Category.FAMILLE);

                service.addContact(Long.parseLong(champId.getText()), champNom.getText(), 
                                   champEmail.getText(), champTel.getText(), "", categories);
                
                JOptionPane.showMessageDialog(null, "Contact ajouté !");
                champId.setText(""); champNom.setText(""); champEmail.setText(""); champTel.setText("");
                chkClient.setSelected(false); chkProspect.setSelected(false); chkAmi.setSelected(false); chkFamille.setSelected(false);
                
                // On simule un clic sur le bouton rafraîchir pour mettre à jour le tableau automatiquement !
                boutonCharger.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur (L'ID doit être un nombre valide et le nom est obligatoire)", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulaire.add(ligne1); formulaire.add(ligne2); formulaire.add(ligne3); formulaire.add(boutonAjouter);
        
        // --- 4. ASSEMBLAGE ---
        panneau.add(panneauHaut, BorderLayout.NORTH); // Les boutons en haut
        panneau.add(scrollPane, BorderLayout.CENTER); // Le tableau au milieu
        panneau.add(formulaire, BorderLayout.SOUTH);  // Le formulaire en bas

        // On lance un chargement initial pour afficher les données dès l'ouverture
        boutonCharger.doClick();

        return panneau;
    }

    // =========================================================================
    // MODULE INTERACTIONS (Version avec Tableau et erreurs propres)
    // =========================================================================
    private static JPanel creerOngletInteractions(InteractionService service) {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // --- 1. CRÉATION DU TABLEAU ---
        String[] colonnes = {"ID", "Type", "Date", "Résumé"};
        javax.swing.table.DefaultTableModel modeleTable = new javax.swing.table.DefaultTableModel(colonnes, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Empêche la modification manuelle des cases
            }
        };

        JTable tableInteractions = new JTable(modeleTable);
        tableInteractions.setFillsViewportHeight(true);
        tableInteractions.setRowHeight(25);
        tableInteractions.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        JScrollPane scrollPane = new JScrollPane(tableInteractions);

        // Petit bonus pro : Un formateur pour afficher la date joliment
        java.text.SimpleDateFormat formateurDate = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm");

        // --- 2. BOUTON RAFRAÎCHIR ---
        JButton boutonCharger = new JButton("🔄 Rafraîchir la liste des interactions");
        boutonCharger.putClientProperty("JButton.buttonType", "default");
        boutonCharger.addActionListener(e -> {
            modeleTable.setRowCount(0); // On vide le tableau
            List<Interaction> inters = service.getAllInteractions();
            for (Interaction i : inters) {
                Object[] ligne = {
                    i.getId(),
                    i.getType(),
                    i.getDate() != null ? formateurDate.format(i.getDate()) : "",
                    i.getSummary()
                };
                modeleTable.addRow(ligne);
            }
        });

        // --- 3. FORMULAIRE D'AJOUT ---
        JPanel formulaire = new JPanel(new GridLayout(3, 1, 10, 10));
        formulaire.setBorder(BorderFactory.createTitledBorder("➕ Ajouter une interaction"));

        JPanel ligne1 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JTextField champId = new JTextField(5);
        JTextField champResume = new JTextField(25);
        ligne1.add(new JLabel("ID Interaction:")); ligne1.add(champId);
        ligne1.add(new JLabel("Résumé:")); ligne1.add(champResume);

        JPanel ligne2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JComboBox<TypeInteraction> comboType = new JComboBox<>(TypeInteraction.values());
        ligne2.add(new JLabel("Type d'échange :")); ligne2.add(comboType);

        JButton boutonAjouter = new JButton("✅ Enregistrer l'interaction");
        boutonAjouter.addActionListener(e -> {
            
            // --- VALIDATION PROPRE AVANT LE TRAITEMENT ---
            String idTexte = champId.getText().trim();
            String resumeTexte = champResume.getText().trim();

            if (idTexte.isEmpty() || resumeTexte.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Veuillez remplir l'ID et le Résumé avant d'enregistrer.", "Champs manquants", JOptionPane.WARNING_MESSAGE);
                return; 
            }

            try {
                Long id = Long.parseLong(idTexte); 
                
                Interaction inter = new Interaction(
                    id, 
                    new Date(), 
                    resumeTexte, 
                    (TypeInteraction) comboType.getSelectedItem()
                );
                service.saveInteraction(inter);
                
                JOptionPane.showMessageDialog(null, "Interaction ajoutée avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                champId.setText(""); champResume.setText("");
                
                boutonCharger.doClick(); 
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "L'ID doit être un nombre valide (ex: 1, 2, 3...).", "Erreur de format", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Erreur métier", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulaire.add(ligne1); formulaire.add(ligne2); formulaire.add(boutonAjouter);

        // --- 4. ASSEMBLAGE ---
        panneau.add(boutonCharger, BorderLayout.NORTH);
        panneau.add(scrollPane, BorderLayout.CENTER);
        panneau.add(formulaire, BorderLayout.SOUTH);

  
        boutonCharger.doClick();

        return panneau;
    }
}