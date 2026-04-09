package addressBook.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ContactTest {

    // Cas nominal : création classique
    @Test
    void shouldCreateValidContact() {
        // Ajout de "1L" pour l'agentId
        Contact contact = new Contact(1L, "Yasmine", "yasmine@mail.com", "0606060606", "Notes", 1L);
        assertNotNull(contact);
        assertEquals("Yasmine", contact.getName());
    }

    // Cas limite : nom vide 
    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new Contact(1L, "", "mail@test.com", "0101", "Notes", 1L);
        });
    }

    // Règle métier : agentId null obligatoire 
    @Test
    void shouldThrowExceptionWhenAgentIdIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new Contact(1L, "Yasmine", "mail@test.com", "0101", "Notes", null);
        });
        assertEquals("Erreur : L'ID de l'agent est obligatoire !", exception.getMessage());
    }
}