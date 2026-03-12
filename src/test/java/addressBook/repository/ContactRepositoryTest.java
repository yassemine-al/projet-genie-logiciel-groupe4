package addressBook.repository;

import adressBook.entités.Contact;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactRepositoryTest {

    private MemoryContact repository;
    private final String TEST_FILE = "test_contacts.json";

    @BeforeEach
    void setUp() {
        // Avant chaque test, on initialise le repository avec le fichier de test
        repository = new MemoryContact(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        // Après chaque test, on supprime le fichier pour repartir à zéro
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    // 1. Test "Round-trip basique"
    @Test
    void testRoundTripBasique() {
        // ÉTAPE 1 : Préparation (avec le constructeur à 5 paramètres de ton collègue)
        Contact contact = new Contact(1L, "Dupont", "dupont@test.com", "0601020304", "Un super collègue"); 
        
        // ÉTAPE 2 : Action (Sauvegarde)
        repository.save(contact);

        // ÉTAPE 3 : Vérification (Relecture)
        List<Contact> contactsEnregistres = repository.findAll();
        
        assertEquals(1, contactsEnregistres.size(), "Il doit y avoir 1 contact dans la liste");
        assertEquals("Dupont", contactsEnregistres.get(0).getName(), "Le nom doit correspondre");
    }

    // 2. Test "Fichier inexistant"
    @Test
    void testFichierInexistant() {
        // ÉTAPE 1 : Action (On lit directement sans avoir créé de fichier)
        List<Contact> contacts = repository.findAll();

        // ÉTAPE 2 : Vérification (Ça ne doit pas planter et renvoyer une liste vide)
        assertNotNull(contacts, "La liste ne doit pas être null");
        assertTrue(contacts.isEmpty(), "La liste doit être vide si le fichier n'existe pas");
    }

    // 3. Test "Cas limite" (caractères spéciaux)
    @Test
    void testContactAvecCaracteresSpeciaux() {
        // ÉTAPE 1 : Préparation avec des caractères complexes
        Contact contactBizarre = new Contact(2L, "O'Connor-García !@#", "test@bizarre.com", "+33-6-00", "Notes \n spéciales");
        
        // ÉTAPE 2 : Action
        repository.save(contactBizarre);

        // ÉTAPE 3 : Vérification
        List<Contact> contacts = repository.findAll();
        assertEquals(1, contacts.size());
        assertEquals("O'Connor-García !@#", contacts.get(0).getName(), "Les caractères spéciaux doivent être bien lus");
    }

}