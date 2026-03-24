package addressBook.repository;

import adressBook.entités.Contact;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implémentation du Repository par le Membre D
 */
public class MemoryContact implements ContactRepository {

    // liste de stocker les données en mémoire
    private List<Contact> contacts = new ArrayList<>();

    @Override
    public Contact save(Contact contact) {
        // on le supprime avant de rajouter la nouvelle version
        this.findById(contact.getId()).ifPresent(c -> contacts.remove(c));
        contacts.add(contact);
        return contact;
    }

    @Override
    public List<Contact> findAll() {
        // On retourne une copie pour éviter de modifier la liste originale par erreur ailleurs
        return new ArrayList<>(contacts);
    }

    @Override
    public Optional<Contact> findById(Long id) {
        // On cherche le contact par son ID
        return contacts.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void delete(Long id) {
        // Suppression par ID
        contacts.removeIf(c -> c.getId().equals(id));
    }
}