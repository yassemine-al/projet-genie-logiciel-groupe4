package addressBook.service;

import java.util.List;
import java.util.Optional;

import addressBook.entites.Interaction;
import addressBook.repository.InteractionRepository;

public class InteractionService {
    private final InteractionRepository repository;

    public InteractionService(InteractionRepository repository) {
        this.repository = repository;
    }

    public Interaction saveInteraction(Interaction interaction) {
        if (interaction.getSummary() == null || interaction.getSummary().trim().isEmpty()) {
            throw new IllegalArgumentException("Le résumé est obligatoire");
        }
        return repository.save(interaction);
    }

    public List<Interaction> getAllInteractions() { return repository.findAll(); }
    public Optional<Interaction> getInteractionById(Long id) { return repository.findById(id); }
    public void deleteInteraction(Long id) { repository.deleteById(id); }
}