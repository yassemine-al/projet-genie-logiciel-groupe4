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
        Contact contact = new Contact(1L, "Dupont", "dupont@test.com", "0601020304", "Un super collègue"); 
        repository.save(contact);

        List<Contact> contactsEnregistres = repository.findAll();
        assertEquals(1, contactsEnregistres.size(), "Il doit y avoir 1 contact dans la liste");
        assertEquals("Dupont", contactsEnregistres.get(0).getName(), "Le nom doit correspondre");
    }

    // 2. Test "Fichier inexistant"
    @Test
    void testFichierInexistant() {
        List<Contact> contacts = repository.findAll();
        assertNotNull(contacts, "La liste ne doit pas être null");
        assertTrue(contacts.isEmpty(), "La liste doit être vide si le fichier n'existe pas");
    }

    // 3. Test "Cas limite" (caractères spéciaux)
    @Test
    void testContactAvecCaracteresSpeciaux() {
        Contact contactBizarre = new Contact(2L, "O'Connor-García !@#", "test@bizarre.com", "+33-6-00", "Notes \n spéciales");
        repository.save(contactBizarre);

        List<Contact> contacts = repository.findAll();
        assertEquals(1, contacts.size());
        assertEquals("O'Connor-García !@#", contacts.get(0).getName(), "Les caractères spéciaux doivent être bien lus");
    }

    // 4. Test de la recherche par ID (NOUVEAU)
    @Test
    void testFindById() {
        Contact contact = new Contact(3L, "Alice", "alice@test.com", "0612345678", "Amie");
        repository.save(contact);

        assertTrue(repository.findById(3L).isPresent(), "On doit trouver le contact avec l'ID 3");
        assertFalse(repository.findById(99L).isPresent(), "On ne doit pas trouver l'ID 99");
    }

    // 5. Test de la suppression (NOUVEAU)
    @Test
    void testDelete() {
        Contact contact = new Contact(4L, "Bob", "bob@test.com", "0687654321", "Collègue");
        repository.save(contact);
        
        repository.delete(4L);

        List<Contact> contacts = repository.findAll();
        assertTrue(contacts.isEmpty(), "La liste doit être vide après la suppression");
    }

    // 6. Test de tous les Getters de l'entité Contact (NOUVEAU)
    @Test
    void testTousLesGetters() {
        Contact contact = new Contact(5L, "Charlie", "charlie@test.com", "0700000000", "Notes de test");
        
        assertEquals(5L, contact.getId());
        assertEquals("Charlie", contact.getName());
        assertEquals("charlie@test.com", contact.getEmail());
        assertEquals("0700000000", contact.getPhone());
        assertEquals("Notes de test", contact.getNotes());
    }
}