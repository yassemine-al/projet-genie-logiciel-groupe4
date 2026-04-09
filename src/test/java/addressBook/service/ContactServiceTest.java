package addressBook.service;

import addressBook.entities.Contact;
import addressBook.entities.Category;
import addressBook.repository.ContactRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository; 

    @InjectMocks
    private ContactService contactService; 

    @Test
    void shouldCallSaveWhenAddingContact() {
        // Préparation 
        Set<Category> categories = new HashSet<>();
        categories.add(Category.CLIENT);
        Long agentId = 10L;

        Contact fakeContact = new Contact(1L, "Kyachou", "kyachou@test.com", "0102", "Note", agentId);
        when(contactRepository.save(any(Contact.class))).thenReturn(fakeContact);

        // Appel du service
        Contact result = contactService.addContact(1L, "Kyachou", "kyachou@test.com", "0102", "Note", categories, agentId);

        // Vérifications
        assertNotNull(result);
        assertEquals("Kyachou", result.getName());
        assertEquals(agentId, result.getAgentId());
        verify(contactRepository, times(1)).save(any(Contact.class));
    }
    @Test
    void shouldFilterContactsByAgentId() {
        // Préparation 
        Contact c1 = new Contact(1L, "Client_A", "a@a.com", "01", "Note", 1L);
        Contact c2 = new Contact(2L, "Client_B", "b@b.com", "02", "Note", 2L);
        when(contactRepository.findAll()).thenReturn(List.of(c1, c2));

        // Test 
        List<Contact> result = contactService.getContactsByAgent(1L);

        // Vérification
        assertEquals(1, result.size(), "On ne doit trouver qu'un seul contact");
        assertEquals("Client_A", result.get(0).getName(), "Le contact doit être Client_A");
        verify(contactRepository, times(1)).findAll();
    }

    @Test
    void searchContactsByNameShouldFilterByAgentId() {
        // Préparation 
        Contact alice1 = new Contact(1L, "Alice_Admin", "a@a.com", "01", "Note", 1L);
        Contact alice2 = new Contact(2L, "Alice_User", "b@b.com", "02", "Note", 2L);
        when(contactRepository.findAll()).thenReturn(List.of(alice1, alice2));

        // Test 
        List<Contact> result = contactService.searchContactsByName("Alice", 1L);

        // Vérification 
        assertEquals(1, result.size());
        assertEquals("Alice_Admin", result.get(0).getName());
    }

    // Test cas nominal 
    @Test
    void testDeleteContact() {
        contactService.deleteContact(1L);
        verify(contactRepository, times(1)).delete(1L);
    }
}