package addressBook.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import adressBook.entités.Contact;
import java.util.Optional;

class MemoryContactTest {

    private MemoryContact repository;

    @BeforeEach
    void setUp() {
        // On crée une nouvelle instance propre pour chaque test
        repository = new MemoryContact();
    }

    /**
     * TEST 1 :
     * On vérifie qu'un contact sauvegardé est bien récupérable.
     */
    @Test
    void testSaveAndFindById() {
        Contact contact = new Contact(1L, "Jean Dupont", "jean@test.com", "0601020304", "Ami de fac");
        repository.save(contact);

        Optional<Contact> found = repository.findById(1L);
        
        assertTrue(found.isPresent(), "Le contact devrait exister");
        assertEquals("Jean Dupont", found.get().getName());
    }

    /**
     * TEST 2 :
     * On vérifie que le système ne plante pas et renvoie 'Empty'.
     */
    @Test
    void testFindByIdInexistant() {
        Optional<Contact> found = repository.findById(99L);
        assertFalse(found.isPresent());
    }


    /**
     * TEST 3 : 
     * Cas limite - Liste vide et Caractères spéciaux
     */
    @Test
    void testValidationNomVide() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(2L, "", "mail@test.com", "0000", "notes");
        });
    }
}


