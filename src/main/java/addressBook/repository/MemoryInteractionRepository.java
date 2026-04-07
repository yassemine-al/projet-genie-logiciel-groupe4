package addressBook.repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import addressBook.entites.Interaction;

public class MemoryInteractionRepository implements InteractionRepository {
    private List<Interaction> interactions = new ArrayList<>();
    private Long currentId = 1L; // Pour générer les IDs automatiquement

    @Override
    public Interaction save(Interaction interaction) {
        if (interaction.getId() == null) {
            interaction.setId(currentId++); // On donne un ID si l'objet n'en a pas
            interactions.add(interaction);
        } else {
            // Si l'ID existe déjà, on met à jour (optionnel pour ton TP)
            deleteById(interaction.getId());
            interactions.add(interaction);
        }
        return interaction;
    }

    @Override
    public List<Interaction> findAll() {
        return new ArrayList<>(interactions);
    }

    @Override
    public Optional<Interaction> findById(Long id) {
        // Version compatible Java 8 pour éviter le NullPointerException
        return interactions.stream()
                .filter(i -> i.getId() != null && i.getId().equals(id))
                .findFirst();
    }

    @Override
    public void deleteById(Long id) {
        interactions.removeIf(i -> i.getId() != null && i.getId().equals(id));
    }
}