package adressBook.service;

import adressBook.entités.Contact;
import addressBook.repository.ContactRepository;
import java.util.List;
import java.util.Optional;

public class ContactService {
    
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    public Contact addContact(Long id, String name, String email, String phone, String notes) {
        Contact contact = new Contact(id, name, email, phone, notes);
        return contactRepository.save(contact);
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public void deleteContact(Long id) {
        contactRepository.delete(id);
    }
} 