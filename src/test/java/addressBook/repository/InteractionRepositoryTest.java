package addressBook.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import adressBook.entités.Interaction;
import java.util.Optional;

public class InteractionRepositoryTest {
	
    private InteractionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryInteractionRepository();
    }

    @Test
    void testRoundTrip_Basique() {
        Interaction inter = new Interaction();
        inter.setSummary("Test");
        Interaction saved = repository.save(inter);
        
        Optional<Interaction> found = repository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getSummary());
    }

    @Test
    void testFindById_Inexistant() {
        Optional<Interaction> result = repository.findById(999L);
        assertFalse(result.isPresent());
    }

    @Test
    void testSauvegardeCaracteresSpeciaux() {
        // on teste un cas limite : un résumé avec plein de symboles et d'accents
        Interaction nouvelleInteraction = new Interaction();
        String texteComplexe = "Appel à 14h30 avec l'équipe n°1 @Paris !! #urgent";
        nouvelleInteraction.setSummary(texteComplexe);
        
        Interaction resultat = repository.save(nouvelleInteraction);
        
        Optional<Interaction> recherche = repository.findById(resultat.getId());
        
        assertTrue(recherche.isPresent());
        assertEquals(texteComplexe, recherche.get().getSummary());
    }
}