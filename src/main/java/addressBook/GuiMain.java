package addressBook;

import addressBook.entities.*;
import addressBook.repository.*;
import addressBook.service.*;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GuiMain {
    
    // GESTION DES AGENTS 
    private static List<SaleAgent> listeDesAgents = new ArrayList<>();
    private static SaleAgent agentSession;
    private static JLabel labelAgentStatus = new JLabel();

    // création de l'admin par défaut au lancement
    static {
        listeDesAgents.add(new SaleAgent(1L, "Admin_Pro", "admin123"));
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
            UIManager.put("Button.arc", 10);
            UIManager.put("Component.arc", 10);
        } catch (Exception ex) {
            System.err.println("Erreur thème : " + ex.getMessage());
        }

        // forçage de l'Admin par défaut Si aucun agent n'est connecté
        if (agentSession == null) {
            agentSession = listeDesAgents.get(0);
        }

        SwingUtilities.invokeLater(() -> {
            // INITIALISATION DES SERVICES
            ContactRepository contactRepo = new JsonContactRepository("contacts.json");
            ContactService contactService = new ContactService(contactRepo);
            InteractionService interactionService = new InteractionService(new MemoryInteractionRepository());

           
            JFrame fenetre = new JFrame("CRM Groupe 4 - Multi-Agents");
            fenetre.setSize(1000, 800);
            fenetre.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            fenetre.setLocationRelativeTo(null);
            fenetre.setLayout(new BorderLayout());

            // BARRE DE SESSION (HAUT) 
            JPanel barreSession = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
            barreSession.setBackground(new Color(230, 235, 245));
            
            updateAgentLabel();
            
            JButton btnGestionAgent = new JButton("🔐 Changer / Créer Agent");
            btnGestionAgent.addActionListener(e -> {
                ouvrirMenuAgents(fenetre, args);
            });

            barreSession.add(labelAgentStatus);
            barreSession.add(btnGestionAgent);

            JTabbedPane onglets = new JTabbedPane();
            onglets.addTab("👤 Mes Contacts", creerOngletContacts(contactService));
            onglets.addTab("📅 Mes Interactions", creerOngletInteractions(interactionService, contactService));

            fenetre.add(barreSession, BorderLayout.NORTH);
            fenetre.add(onglets, BorderLayout.CENTER);
            fenetre.setVisible(true);
        });
    }

    /**
     * Menu pour choisir un agent existant ou en créer un nouveau
     */
    private static void ouvrirMenuAgents(JFrame parent, String[] args) {
        String[] options = {"Se connecter (Existant)", "Créer un nouvel Agent", "Annuler"};
        int choix = JOptionPane.showOptionDialog(parent, 
            "Que souhaitez-vous faire ?", "Gestion des Agents",
            JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

        if (choix == 0) { // SE CONNECTER
            JComboBox<String> combo = new JComboBox<>();
            listeDesAgents.forEach(a -> combo.addItem(a.getUsername()));
            
            int res = JOptionPane.showConfirmDialog(parent, combo, "Sélectionnez votre compte :", JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                JPasswordField pf = new JPasswordField();
                int mdpRes = JOptionPane.showConfirmDialog(parent, pf, "Mot de passe :", JOptionPane.OK_CANCEL_OPTION);
                
                if (mdpRes == JOptionPane.OK_OPTION) {
                    SaleAgent selection = listeDesAgents.get(combo.getSelectedIndex());
                    if (selection.checkPassword(new String(pf.getPassword()))) {
                        agentSession = selection;
                        parent.dispose();
                        main(args);
                    } else {
                        JOptionPane.showMessageDialog(parent, "Mot de passe incorrect !");
                    }
                }
            }
        } 
        else if (choix == 1) { // CRÉER UN NOUVEL AGENT
            JTextField fId = new JTextField();
            JTextField fNom = new JTextField();
            JPasswordField fPass = new JPasswordField();
            
            Object[] message = { "ID :", fId, "Nom :", fNom, "Mot de passe :", fPass };
            int res = JOptionPane.showConfirmDialog(parent, message, "Nouveau SaleAgent", JOptionPane.OK_CANCEL_OPTION);
            
            if (res == JOptionPane.OK_OPTION) {
                try {
                    SaleAgent n = new SaleAgent(Long.parseLong(fId.getText()), fNom.getText(), new String(fPass.getPassword()));
                    listeDesAgents.add(n);
                    JOptionPane.showMessageDialog(parent, "Agent créé ! Vous pouvez maintenant vous connecter.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(parent, "Erreur : " + ex.getMessage());
                }
            }
        }
    }

    private static void updateAgentLabel() {
        labelAgentStatus.setText("<html><b>Session :</b> <font color='green'>" 
            + agentSession.getUsername() + "</font> (ID: " + agentSession.getId() + ")</html>");
    }

    private static JPanel creerOngletContacts(ContactService service) {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultTableModel modele = new DefaultTableModel(new String[]{"ID", "Nom", "Email", "Catégories"}, 0);
        JTable table = new JTable(modele);

        Runnable rafraichir = () -> {
            modele.setRowCount(0);
            service.getContactsByAgent(agentSession.getId()).forEach(c -> 
                modele.addRow(new Object[]{c.getId(), c.getName(), c.getEmail(), c.getCategories()}));
        };

        // Formulaire d'ajout
        JPanel form = new JPanel(new FlowLayout());
        JTextField fId = new JTextField(3); JTextField fNom = new JTextField(10);
        JButton btnAdd = new JButton("Ajouter Contact");
        btnAdd.addActionListener(e -> {
            try {
                service.addContact(Long.parseLong(fId.getText()), fNom.getText(), "test@mail.com", "000", "", new HashSet<>(), agentSession.getId());
                rafraichir.run();
            } catch (Exception ex) { JOptionPane.showMessageDialog(null, ex.getMessage()); }
        });
        form.add(new JLabel("ID:")); form.add(fId); form.add(new JLabel("Nom:")); form.add(fNom); form.add(btnAdd);

        panneau.add(new JScrollPane(table), BorderLayout.CENTER);
        panneau.add(form, BorderLayout.SOUTH);
        rafraichir.run();
        return panneau;
    }

    private static JPanel creerOngletInteractions(InteractionService service, ContactService contactService) {
        JPanel panneau = new JPanel(new BorderLayout(10, 10));
        panneau.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        DefaultTableModel modele = new DefaultTableModel(new String[]{"ID", "Résumé"}, 0);
        JTable table = new JTable(modele);
        
        JComboBox<String> combo = new JComboBox<>();
        contactService.getContactsByAgent(agentSession.getId()).forEach(c -> combo.addItem(c.getName()));

        JButton btn = new JButton("Enregistrer Interaction");
        btn.addActionListener(e -> {
            if(combo.getSelectedItem() == null) return;
            Interaction i = new Interaction((long)(Math.random()*1000), new Date(), "Echange avec " + combo.getSelectedItem(), TypeInteraction.APPEL);
            service.saveInteraction(i);
            modele.addRow(new Object[]{i.getId(), i.getSummary()});
        });

        panneau.add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel p = new JPanel(); p.add(new JLabel("Contact :")); p.add(combo); p.add(btn);
        panneau.add(p, BorderLayout.SOUTH);
        return panneau;
    }
}