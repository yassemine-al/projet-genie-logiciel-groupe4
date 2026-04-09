package addressBook.service;

import addressBook.entities.Contact;
import addressBook.repository.ContactRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        Contact fakeContact = new Contact(1L, "Kyachou", "kyachou@test.com", "0102", "Note");
        when(contactRepository.save(any(Contact.class))).thenReturn(fakeContact);

        // Ajout du paramètre null à la fin
        Contact result = contactService.addContact(1L, "Kyachou", "kyachou@test.com", "0102", "Note", null);

        assertNotNull(result);
        assertEquals("Kyachou", result.getName());
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            // Ajout du paramètre null à la fin
            contactService.addContact(1L, "", "email@test.com", "01", "Notes", null);
        });
        
        verify(contactRepository, never()).save(any());
    }

    @Test
    void testDeleteContact() {
        contactService.deleteContact(1L);
        verify(contactRepository, times(1)).delete(1L);
    }
}