package addressBook.service; 

import addressBook.entities.Contact;
import addressBook.entities.Category;
import addressBook.repository.ContactRepository;
import java.util.List;
import java.util.Optional;
import java.util.Set;


public  class ContactService { 
    private final ContactRepository contactRepository;

    public ContactService(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    public Contact addContact(Long id, String name, String email, String phone, 
                              String notes, Set<Category> categories, Long agentId) {
        Contact contact = new Contact(id, name, email, phone, notes, agentId);
        
        if (categories != null) {
            for (Category cat : categories) {
                contact.addCategory(cat);
            }
        }
        
        return contactRepository.save(contact);
    }

    // Filtrage pour que l'agent ne voie que ses propres contacts
    public List<Contact> getContactsByAgent(Long agentId) {
        return contactRepository.findAll().stream()
                .filter(c -> c.getAgentId().equals(agentId))
                .toList();
    }

    // Recherche par nom filtrée par agent
    public List<Contact> searchContactsByName(String name, Long agentId) {
        if (name == null || name.isBlank()) {
            return List.of(); 
        }
        return contactRepository.findAll().stream()
                .filter(c -> c.getAgentId().equals(agentId))
                .filter(c -> c.getName().toLowerCase().contains(name.toLowerCase()))
                .toList();
    }

    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }

    public void deleteContact(Long id) {
        contactRepository.delete(id);
    }
}