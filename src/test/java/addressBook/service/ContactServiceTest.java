package addressBook.service;

import addressBook.entites.Contact;
import addressBook.repository.ContactRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
//Active Mockito pour JUnit 5
@ExtendWith(MockitoExtension.class) // Active Mockito pour JUnit 5
class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository; 

    @InjectMocks
    private ContactService contactService; 

    @Test
    void shouldCallSaveWhenAddingContact() {
        // Arrange
        Contact fakeContact = new Contact(1L, "Kyachou", "kyachou@test.com", "0102", "Note");
        when(contactRepository.save(any(Contact.class))).thenReturn(fakeContact);

        // Act
        Contact result = contactService.addContact(1L, "Kyachou", "kyachou@test.com", "0102", "Note");

        // Assert
        assertNotNull(result);
        assertEquals("Kyachou", result.getName());
        // Vérification 
        verify(contactRepository, times(1)).save(any(Contact.class));
    }

    @Test
    void shouldThrowExceptionWhenNameIsEmpty() {
        // Test d'un cas limite
        assertThrows(IllegalArgumentException.class, () -> {
            contactService.addContact(1L, "", "email@test.com", "01", "Notes");
        });
        
        // Vérification 
        verify(contactRepository, never()).save(any());
    }
    @Test
    void testDeleteContact() {
        // Act
        contactService.deleteContact(1L);

        // Vérification 
        verify(contactRepository, times(1)).delete(1L);
    }
}