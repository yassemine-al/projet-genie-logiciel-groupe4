package adressBook.entités;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ContactTest {

    // Cas nominal : création classique 
    @Test
    void shouldCreateValidContact() {
        Contact contact = new Contact(1L, "Yasmine", "yasmine@mail.com", "0606060606", "Notes");
        assertNotNull(contact);
        assertEquals("Yasmine", contact.getName());
    }

    // Cas limite : nom vide 
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(1L, "", "mail@test.com", "0101", "Notes");
        });
    }

    // Règle métier : nom null obligatoire 
    @Test
    void shouldThrowExceptionWhenNameIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Contact(1L, null, "mail@test.com", "0101", "Notes");
        });
        assertEquals("Erreur : Le nom du contact est obligatoire !", exception.getMessage());
    }
}