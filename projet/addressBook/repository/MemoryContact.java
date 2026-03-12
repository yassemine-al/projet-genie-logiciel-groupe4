package addressBook.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import adressBook.entités.Contact;


public class MemoryContact implements ContactRepository {

    private final List<Contact> contacts = new ArrayList<>();

    @Override
    public Contact save(Contact contact) {
        // Ajout d'un contact à la liste
        this.contacts.add(contact);
        return contact;
    }

    @Override
    public List<Contact> findAll() {
        // nouvelle liste contenant tous les contacts
        return new ArrayList<>(this.contacts);
    }

    @Override
    public Optional<Contact> findById(Long id) {
        // recherche du contact qui a l'ID correspondant
        return contacts.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(Long id) {
        // On supprime le contact si l'ID correspond
        contacts.removeIf(c -> c.getId().equals(id));
    }
}

