package addressBook.service;

import addressBook.entities.Contact;
import addressBook.entities.Category;
import addressBook.repository.ContactRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ContactService {
    
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact addContact(Long id, String name, String email, String phone, String notes, Set<Category> categories) {
        Contact contact = new Contact(id, name, email, phone, notes);
        
        if (categories != null) {
            for (Category cat : categories) {
                contact.addCategory(cat);
            }
        }
        
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public List<Contact> searchContactsByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of(); 
        }
        return contactRepository.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public void deleteContact(Long id) {
        contactRepository.delete(id);
    }
}