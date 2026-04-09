package addressBook;

import addressBook.entities.*;
import addressBook.repository.*;
import addressBook.service.*;

import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuiMain {
    public static void main(String[] args) {
        System.out.println("Démarrage du programme...");

        // --- 1. INITIALISATION DES SERVICES ---
        ContactRepository contactRepo = new JsonContactRepository("contacts.json");
        ContactService contactService = new ContactService(contactRepo);

        InteractionRepository interactionRepo = new MemoryInteractionRepository();
        InteractionService interactionService = new InteractionService(interactionRepo);

        System.out.println("Services initialisés ! Création de la fenêtre...");

        // --- 2. FENÊTRE PRINCIPALE ---
        JFrame fenetre = new JFrame("CRM Groupe 4 - Application Complète");
        fenetre.setSize(800, 600);
        fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenetre.setLocationRelativeTo(null);

        // Création du gestionnaire d'onglets
        JTabbedPane onglets = new JTabbedPane();

        // --- 3. CRÉATION DES ONGLETS ---
        onglets.addTab("👤 Gérer les Contacts", creerOngletContacts(contactService));
        onglets.addTab("📅 Gérer les Interactions", creerOngletInteractions(interactionService));

        // --- 4. ASSEMBLAGE ET AFFICHAGE ---
        fenetre.add(onglets);
        fenetre.setVisible(true);

        System.out.println("Fenêtre affichée avec succès !");
    }

    // =========================================================================
    // MODULE CONTACTS
    // =========================================================================
    private static JPanel creerOngletContacts(ContactService service) {
        JPanel panneau = new JPanel(new BorderLayout());

        JTextArea zoneAffichage = new JTextArea();
        zoneAffichage.setEditable(false);
        zoneAffichage.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(zoneAffichage);

        JButton boutonCharger = new JButton("🔄 Rafraîchir la liste des contacts");
        boutonCharger.addActionListener(e -> {
            zoneAffichage.setText("");
            List<Contact> contacts = service.getAllContacts();
            if (contacts.isEmpty()) {
                zoneAffichage.append("Aucun contact trouvé.\n");
            } else {
                for (Contact c : contacts) {
                    zoneAffichage.append("👤 " + c.getName() + " (ID: " + c.getId() + ") | Email: " + c.getEmail() + "\n");
                    zoneAffichage.append("   Catégories : " + c.getCategories() + "\n");
                    zoneAffichage.append("--------------------------------------------------\n");
                }
            }
        });

        JPanel formulaire = new JPanel(new GridLayout(4, 1));
        formulaire.setBorder(BorderFactory.createTitledBorder("➕ Ajouter un contact"));

        JPanel ligne1 = new JPanel();
        JTextField champId = new JTextField(5);
        JTextField champNom = new JTextField(15);
        ligne1.add(new JLabel("ID:")); ligne1.add(champId);
        ligne1.add(new JLabel("Nom:")); ligne1.add(champNom);

        JPanel ligne2 = new JPanel();
        JTextField champEmail = new JTextField(15);
        JTextField champTel = new JTextField(10);
        ligne2.add(new JLabel("Email:")); ligne2.add(champEmail);
        ligne2.add(new JLabel("Tél:")); ligne2.add(champTel);

        JPanel ligne3 = new JPanel();
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
                boutonCharger.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur (L'ID doit être un nombre)", "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulaire.add(ligne1); formulaire.add(ligne2); formulaire.add(ligne3); formulaire.add(boutonAjouter);
        
        panneau.add(boutonCharger, BorderLayout.NORTH);
        panneau.add(scrollPane, BorderLayout.CENTER);
        panneau.add(formulaire, BorderLayout.SOUTH);

        return panneau;
    }

    // =========================================================================
    // MODULE INTERACTIONS
    // =========================================================================
    private static JPanel creerOngletInteractions(InteractionService service) {
        JPanel panneau = new JPanel(new BorderLayout());

        JTextArea zoneAffichage = new JTextArea();
        zoneAffichage.setEditable(false);
        zoneAffichage.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(zoneAffichage);

        JButton boutonCharger = new JButton("🔄 Rafraîchir la liste des interactions");
        boutonCharger.addActionListener(e -> {
            zoneAffichage.setText("");
            List<Interaction> inters = service.getAllInteractions();
            if (inters.isEmpty()) {
                zoneAffichage.append("Aucune interaction trouvée.\n");
            } else {
                for (Interaction i : inters) {
                    zoneAffichage.append("📅 [" + i.getType() + "] ID: " + i.getId() + " - Date: " + i.getDate() + "\n");
                    zoneAffichage.append("   Résumé : " + i.getSummary() + "\n");
                    zoneAffichage.append("--------------------------------------------------\n");
                }
            }
        });

        JPanel formulaire = new JPanel(new GridLayout(3, 1));
        formulaire.setBorder(BorderFactory.createTitledBorder("➕ Ajouter une interaction"));

        JPanel ligne1 = new JPanel();
        JTextField champId = new JTextField(5);
        JTextField champResume = new JTextField(25);
        ligne1.add(new JLabel("ID Interaction:")); ligne1.add(champId);
        ligne1.add(new JLabel("Résumé:")); ligne1.add(champResume);

        JPanel ligne2 = new JPanel();
        JComboBox<TypeInteraction> comboType = new JComboBox<>(TypeInteraction.values());
        ligne2.add(new JLabel("Type d'échange :")); ligne2.add(comboType);

        JButton boutonAjouter = new JButton("✅ Enregistrer l'interaction");
        boutonAjouter.addActionListener(e -> {
            try {
                Interaction inter = new Interaction(
                    Long.parseLong(champId.getText()), 
                    new Date(), 
                    champResume.getText(), 
                    (TypeInteraction) comboType.getSelectedItem()
                );
                service.saveInteraction(inter);
                
                JOptionPane.showMessageDialog(null, "Interaction ajoutée !");
                champId.setText(""); champResume.setText(""); // Corrigé ici
                boutonCharger.doClick();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erreur de saisie : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        formulaire.add(ligne1); formulaire.add(ligne2); formulaire.add(boutonAjouter);

        panneau.add(boutonCharger, BorderLayout.NORTH);
        panneau.add(scrollPane, BorderLayout.CENTER);
        panneau.add(formulaire, BorderLayout.SOUTH);

        return panneau;
    }
}