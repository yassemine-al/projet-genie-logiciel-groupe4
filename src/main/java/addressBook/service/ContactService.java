package addressBook.service;

import addressBook.entites.Contact;
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

    // Cherche tous les contacts dont le nom contient la chaîne demandée (sans tenir compte des majuscules)
    public List<Contact> searchContactsByName(String name) {
        if (name == null || name.isBlank()) {
            return List.of(); // Retourne une liste vide si la recherche est nulle ou vide
        }
        return contactRepository.findAll().stream()
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public void deleteContact(Long id) {
        contactRepository.delete(id);
    }
}