package addressBook.repository;

import adressBook.entités.Contact;
import java.util.List;
import java.util.Optional;

public interface ContactRepository {
    Contact save(Contact contact);
    List<Contact> findAll();
    Optional<Contact> findById(Long id);
    void delete(Long id);
}