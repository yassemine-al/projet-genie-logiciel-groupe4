package addressBook;

import addressBook.entities.*;
import addressBook.repository.*;
import addressBook.service.*;

import java.util.*;

public class Main {
    private static List<SaleAgent> listeAgents = new ArrayList<>();
    private static SaleAgent agentConnecte = null;

    static {
        listeAgents.add(new SaleAgent(1L, "Admin", "admin123"));
    }

    public static void main(String[] args) {
        ContactRepository contactRepo = new JsonContactRepository("contacts.json");
        ContactService contactService = new ContactService(contactRepo);
        InteractionService interactionService = new InteractionService(new MemoryInteractionRepository());

        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== SYSTEME CRM CONSOLE ===");
        while (agentConnecte == null) {
            menuConnexion(scanner);
        }

        boolean quitterApp = false;
        while (!quitterApp) {
            System.out.println("\n--- SESSION : " + agentConnecte.getUsername() + " ---");
            System.out.println("1.Gérer les CONTACTS");
            System.out.println("2.Gérer les INTERACTIONS");
            System.out.println("3.Changer d'Agent");
            System.out.println("4. Quitter");
            System.out.print(" Choix : ");

            String choix = scanner.nextLine();
            switch (choix) {
                case "1" -> menuContacts(contactService, scanner, agentConnecte);
                case "2" -> menuInteractions(interactionService, scanner, contactService, agentConnecte);
                case "3" -> { agentConnecte = null; while(agentConnecte == null) menuConnexion(scanner); }
                case "4" -> quitterApp = true;
            }
        }
        scanner.close();
    }

    // ============================================================
    // MENU CONTACTS 
    // ============================================================
    public static void menuContacts(ContactService contactService, Scanner scanner, SaleAgent agent) {
        System.out.println("\n--- GESTION CONTACTS ---");
        System.out.println("1. Ajouter");
        System.out.println("2. Lister mes contacts");
        System.out.println("3. Rechercher par nom");
        System.out.println("4. Supprimer un contact");
        System.out.print("👉 Choix : ");
        String choix = scanner.nextLine();

        switch (choix) {
            case "1" -> {
                try {
                    System.out.print("ID : "); Long id = Long.parseLong(scanner.nextLine());
                    System.out.print("Nom : "); String nom = scanner.nextLine();
                    contactService.addContact(id, nom, "email@test.com", "000", "", new HashSet<>(), agent.getId());
                    System.out.println("Ajouté !");
                } catch (Exception e) { System.out.println("Erreur : " + e.getMessage()); }
            }
            case "2" -> {
                System.out.println("\n--- MES CONTACTS ---");
                contactService.getContactsByAgent(agent.getId()).forEach(c -> 
                    System.out.println("[" + c.getId() + "] " + c.getName()));
            }
            case "3" -> {
                System.out.print("Entrez le nom (ou une partie) à rechercher : ");
                String recherche = scanner.nextLine();
                List<Contact> resultats = contactService.searchContactsByName(recherche, agent.getId());
                if (resultats.isEmpty()) {
                    System.out.println(" Aucun contact trouvé pour '" + recherche + "'");
                } else {
                    System.out.println("Résultats :");
                    resultats.forEach(c -> System.out.println("- " + c.getName() + " (ID: " + c.getId() + ")"));
                }
            }
            case "4" -> {
                try {
                    System.out.print("Entrez l'ID du contact à supprimer : ");
                    Long idSuppr = Long.parseLong(scanner.nextLine());
                    
                    // Vérification de sécurité : le contact appartient-il bien à l'agent ?
                    Optional<Contact> cible = contactService.getContactById(idSuppr);
                    if (cible.isPresent() && cible.get().getAgentId().equals(agent.getId())) {
                        contactService.deleteContact(idSuppr);
                        System.out.println(" Contact " + idSuppr + " supprimé.");
                    } else {
                        System.out.println(" Action impossible : Contact introuvable ou ne vous appartient pas.");
                    }
                } catch (Exception e) { System.out.println("Erreur : ID invalide."); }
            }
        }
    }


    private static void menuConnexion(Scanner scanner) {
        System.out.println("\n1. Connexion | 2. Créer Agent");
        String c = scanner.nextLine();
        if (c.equals("1")) {
            System.out.print("Nom : "); String n = scanner.nextLine();
            System.out.print("MDP : "); String m = scanner.nextLine();
            listeAgents.stream().filter(a -> a.getUsername().equalsIgnoreCase(n) && a.checkPassword(m))
                .findFirst().ifPresentOrElse(a -> agentConnecte = a, () -> System.out.println("Échec !"));
        } else {
            try {
                System.out.print("ID : "); Long i = Long.parseLong(scanner.nextLine());
                System.out.print("Nom : "); String n = scanner.nextLine();
                System.out.print("MDP : "); String m = scanner.nextLine();
                listeAgents.add(new SaleAgent(i, n, m));
                System.out.println("Agent créé !");
            } catch (Exception e) { System.out.println("Erreur creation."); }
        }
    }

    private static void menuInteractions(InteractionService service, Scanner scanner, ContactService contactService, SaleAgent agent) {
        System.out.println("\n1. Ajouter Interaction | 2. Voir Historique");
        String choix = scanner.nextLine();
        if (choix.equals("1")) {
            List<Contact> mesC = contactService.getContactsByAgent(agent.getId());
            if (mesC.isEmpty()) { System.out.println("Pas de contacts !"); return; }
            for (int i=0; i<mesC.size(); i++) System.out.println(i + ". " + mesC.get(i).getName());
            int idx = Integer.parseInt(scanner.nextLine());
            service.saveInteraction(new Interaction((long)(Math.random()*1000), new Date(), "Echange avec " + mesC.get(idx).getName(), TypeInteraction.APPEL));
            System.out.println("Enregistré !");
        } else {
            service.getAllInteractions().forEach(i -> System.out.println(i.getSummary()));
        }
    }
}