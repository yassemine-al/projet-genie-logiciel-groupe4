package addressBook.repository;

import java.util.List;
import java.util.Optional;

import addressBook.entities.Contact;

public interface ContactRepository {
    Contact save(Contact contact);
    List<Contact> findAll();
    Optional<Contact> findById(Long id);
    void delete(Long id);
}
