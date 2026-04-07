package addressBook.repository;

import java.util.List;
import java.util.Optional;

import addressBook.entities.Interaction;

public interface InteractionRepository {
    Interaction save(Interaction interaction);
    List<Interaction> findAll();
    Optional<Interaction> findById(Long id);
    void deleteById(Long id);
}