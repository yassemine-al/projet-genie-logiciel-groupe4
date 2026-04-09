package addressBook.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import addressBook.entities.Contact;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactRepositoryTest {

    private JsonContactRepository repository;
    private final String TEST_FILE = "test_contacts.json";
    private final Long TEST_AGENT_ID = 1L; 

    @BeforeEach
    void setUp() {
        repository = new JsonContactRepository(TEST_FILE);
    }

    @AfterEach
    void tearDown() {
        File file = new File(TEST_FILE);
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    void testRoundTripBasique() {
        Contact contact = new Contact(1L, "Dupont", "dupont@test.com", "0601020304", "Un super collègue", TEST_AGENT_ID); 
        repository.save(contact);

        List<Contact> contactsEnregistres = repository.findAll();
        assertEquals(1, contactsEnregistres.size());
        assertEquals("Dupont", contactsEnregistres.get(0).getName());
        assertEquals(TEST_AGENT_ID, contactsEnregistres.get(0).getAgentId(), "L'ID de l'agent doit être persisté dans le JSON");
    }

    @Test
    void testFichierInexistant() {
        List<Contact> contacts = repository.findAll();
        assertNotNull(contacts);
        assertTrue(contacts.isEmpty());
    }

    @Test
    void testContactAvecCaracteresSpeciaux() {
        // Ajout de TEST_AGENT_ID
        Contact contactBizarre = new Contact(2L, "O'Connor-García !@#", "test@bizarre.com", "+33-6-00", "Notes \n spéciales", TEST_AGENT_ID);
        repository.save(contactBizarre);

        List<Contact> contacts = repository.findAll();
        assertEquals(1, contacts.size());
        assertEquals("O'Connor-García !@#", contacts.get(0).getName());
    }

    @Test
    void testFindById() {
        Contact contact = new Contact(3L, "Alice", "alice@test.com", "0612345678", "Amie", TEST_AGENT_ID);
        repository.save(contact);

        assertTrue(repository.findById(3L).isPresent());
        assertFalse(repository.findById(99L).isPresent());
    }

    @Test
    void testDelete() {
        Contact contact = new Contact(4L, "Bob", "bob@test.com", "0687654321", "Collègue", TEST_AGENT_ID);
        repository.save(contact);
        
        repository.delete(4L);

        List<Contact> contacts = repository.findAll();
        assertTrue(contacts.isEmpty());
    }

    @Test
    void testTousLesGetters() {
        Contact contact = new Contact(5L, "Charlie", "charlie@test.com", "0700000000", "Notes de test", TEST_AGENT_ID);
        
        assertEquals(5L, contact.getId());
        assertEquals("Charlie", contact.getName());
        assertEquals("charlie@test.com", contact.getEmail());
        assertEquals("0700000000", contact.getPhone());
        assertEquals("Notes de test", contact.getNotes());
        assertEquals(TEST_AGENT_ID, contact.getAgentId());
    }
}