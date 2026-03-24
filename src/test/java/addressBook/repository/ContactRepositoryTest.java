package addressBook.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import adressBook.entités.Contact;
import java.util.List;

class ContactRepositoryTest {

    private MemoryContact repository;

    @BeforeEach
    void setUp() {
        // Correction ici : pas de paramètre entre les parenthèses !
        repository = new MemoryContact();
    }

    @Test
    void testSaveAndUpdate() {
        // Test de la mise à jour (ton code remove puis add)
        Contact c1 = new Contact(1L, "Original", "test@test.com", "0101", "Notes");
        repository.save(c1);
        
        Contact c2 = new Contact(1L, "Modifié", "test@test.com", "0101", "Notes");
        repository.save(c2);
        
        assertEquals(1, repository.findAll().size(), "On ne doit avoir qu'un seul contact");
        assertEquals("Modifié", repository.findById(1L).get().getName());
    }

    @Test
    void testDelete() {
        // Test de ta fonction delete
        Contact c1 = new Contact(5L, "A supprimer", "suppr@test.com", "00", "");
        repository.save(c1);
        
        repository.delete(5L);
        
        assertTrue(repository.findById(5L).isEmpty(), "Le contact doit être supprimé");
        assertEquals(0, repository.findAll().size());
    }

    @Test
    void testFindAllReturnsCopy() {
        // Test pour vérifier que ta liste est protégée (encapsulation)
        repository.save(new Contact(1L, "Test", "t@t.com", "00", ""));
        List<Contact> contacts = repository.findAll();
        contacts.clear(); // On vide la copie
        
        assertFalse(repository.findAll().isEmpty(), "La liste originale dans le repository ne doit pas être vide !");
    }
}