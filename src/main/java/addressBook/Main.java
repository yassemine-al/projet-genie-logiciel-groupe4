package addressBook;

import addressBook.entities.*;
import addressBook.repository.*;
import addressBook.service.*;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // --- 1. INITIALISATION DES SERVICES ---
        ContactRepository contactRepo = new JsonContactRepository("contacts.json");
        ContactService contactService = new ContactService(contactRepo);

        InteractionRepository interactionRepo = new MemoryInteractionRepository();
        InteractionService interactionService = new InteractionService(interactionRepo);

        Scanner scanner = new Scanner(System.in);
        boolean quitterApp = false;

        while (!quitterApp) {
            System.out.println("\n===========================================");
            System.out.println("     SYSTEME CRM - GESTION GLOBALE       ");
            System.out.println("===========================================");
            System.out.println("1. 👤 Gérer les CONTACTS");
            System.out.println("2. 📅 Gérer les INTERACTIONS");
            System.out.println("3. ❌ Quitter l'application");
            System.out.print("👉 Votre choix : ");

            String choix = scanner.nextLine();

            switch (choix) {
                case "1":
                    menuContacts(contactService, scanner);
                    break;
                case "2":
                    menuInteractions(interactionService, scanner);
                    break;
                case "3":
                    quitterApp = true;
                    System.out.println("👋 Fin du programme. Au revoir !");
                    break;
                default:
                    System.out.println("⚠️ Choix invalide.");
            }
        }
        scanner.close();
    }

    // ============================================================
    // MODULE CONTACTS
    // ============================================================
    private static void menuContacts(ContactService service, Scanner scanner) {
        System.out.println("\n--- MENU CONTACTS ---");
        System.out.println("1. Ajouter un contact");
        System.out.println("2. Lister tous les contacts");
        System.out.print("👉 Choix : ");
        String choix = scanner.nextLine();

        if (choix.equals("1")) {
            try {
                System.out.print("ID : ");
                Long id = Long.parseLong(scanner.nextLine());
                System.out.print("Nom : ");
                String nom = scanner.nextLine();
                System.out.print("Email : ");
                String email = scanner.nextLine();
                System.out.print("Téléphone : ");
                String tel = scanner.nextLine();
                System.out.print("Notes : ");
                String notes = scanner.nextLine();

                Set<Category> cats = new HashSet<>();
                boolean fini = false;
                while (!fini) {
                    System.out.println("Catégories : " + cats + " | 1.CLIENT 2.PROSPECT 3.AMI 4.FAMILLE 5.FINIR");
                    System.out.print("👉 Choix : ");
                    String catInput = scanner.nextLine();
                    switch (catInput) {
                        case "1": cats.add(Category.CLIENT); break;
                        case "2": cats.add(Category.PROSPECT); break;
                        case "3": cats.add(Category.AMI); break;
                        case "4": cats.add(Category.FAMILLE); break;
                        default: fini = true; break;
                    }
                }
                service.addContact(id, nom, email, tel, notes, cats);
                System.out.println("✅ Contact enregistré avec succès !");
            } catch (Exception e) {
                System.out.println("⚠️ Erreur : " + e.getMessage());
            }
        } else if (choix.equals("2")) {
            System.out.println("\n--- LISTE DES CONTACTS ---");
            service.getAllContacts().forEach(c -> 
                System.out.println("ID " + c.getId() + " : " + c.getName() + " | Cats: " + c.getCategories()));
        }
    }

    // ============================================================
    // MODULE INTERACTIONS
    // ============================================================
    private static void menuInteractions(InteractionService service, Scanner scanner) {
        System.out.println("\n--- MENU INTERACTIONS ---");
        System.out.println("1. Ajouter une interaction");
        System.out.println("2. Lister les interactions");
        System.out.print("👉 Choix : ");
        String choix = scanner.nextLine();

        if (choix.equals("1")) {
            try {
                System.out.print("ID de l'interaction : ");
                Long id = Long.parseLong(scanner.nextLine());
                System.out.print("Résumé : ");
                String resume = scanner.nextLine();
                
                System.out.println("Type : 1.APPEL | 2.EMAIL | 3.REUNION | 4.MESSAGE");
                System.out.print("👉 Choix : ");
                String typeInput = scanner.nextLine();
                TypeInteraction type = switch(typeInput) {
                    case "1" -> TypeInteraction.APPEL;
                    case "2" -> TypeInteraction.EMAIL;
                    case "3" -> TypeInteraction.REUNION;
                    default -> TypeInteraction.MESSAGE;
                };

                // Création de l'interaction avec la date du jour
                Interaction inter = new Interaction(id, new Date(), resume, type);
                service.saveInteraction(inter);
                System.out.println("✅ Interaction enregistrée !");
            } catch (Exception e) {
                System.out.println("⚠️ Erreur : " + e.getMessage());
            }
        } else if (choix.equals("2")) {
            System.out.println("\n--- LISTE DES INTERACTIONS ---");
            service.getAllInteractions().forEach(i -> 
                System.out.println("[" + i.getType() + "] " + i.getDate() + " : " + i.getSummary()));
        }
    }
}