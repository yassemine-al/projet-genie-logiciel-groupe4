package addressBook.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import addressBook.entities.Interaction;
import addressBook.entities.TypeInteraction; 
import java.util.Date; 
import java.util.Optional;

public class InteractionRepositoryTest {
    
    private InteractionRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MemoryInteractionRepository();
    }

    @Test
    void testRoundTrip_Basique() {
        
        Interaction inter = new Interaction(1L, new Date(), "Test", TypeInteraction.values()[0]);
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
        String texteComplexe = "Appel à 14h30 avec l'équipe n°1 @Paris !! #urgent";
 
        Interaction nouvelleInteraction = new Interaction(2L, new Date(), texteComplexe, TypeInteraction.values()[0]);
        
        Interaction resultat = repository.save(nouvelleInteraction);
        
        Optional<Interaction> recherche = repository.findById(resultat.getId());
        
        assertTrue(recherche.isPresent());
        assertEquals(texteComplexe, recherche.get().getSummary());
    }
}