package addressBook.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import adressBook.entités.Contact; 

public class MemoryContact implements ContactRepository {

    private final List<Contact> contacts = new ArrayList<>();

    // Constructeur éviter les conflit vu que Yasmine en a mis dans le main en cas de test
    public MemoryContact() {
    }

    // Ajouter pour éviter les conflit vu que Yasmine en a mis dans le main
    public MemoryContact(String filePath) {
        this();
    }

    @Override
    public Contact save(Contact contact) {
        // On n'ajoute pas si le contact est null
        if (contact == null) return null;

        // Éviter les doublons : si l'ID est déjà présent, on ne l'ajoute pas
        if (contact.getId() != null && findById(contact.getId()).isPresent()) {
            return contact; 
        }

        this.contacts.add(contact);
        return contact;
    }

    @Override
    public Optional<Contact> findById(Long id) {
        // Si l'ID est null, on retourne un Optional vide au lieu de faire planter le stream
        if (id == null) {
            return Optional.empty();
        }
        
        return contacts.stream()
                .filter(c -> id.equals(c.getId()))
                .findFirst();
    }

    // Recherche par nom
    public List<Contact> findByName(String name) {
        if (name == null || name.isEmpty()) {
            return new ArrayList<>();
        }
        return contacts.stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .toList();
    }