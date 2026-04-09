package addressBook.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import addressBook.entities.Interaction;
import addressBook.entities.TypeInteraction;
import addressBook.repository.InteractionRepository;

@ExtendWith(MockitoExtension.class)
public class InteractionServiceTest {

    @Mock
    private InteractionRepository repository;

    @InjectMocks
    private InteractionService service;

    @Test
    void testSave_Nominal() {
        
        Interaction inter = new Interaction(1L, new Date(), "Appel commercial", TypeInteraction.values()[0]);
        
        when(repository.save(any(Interaction.class))).thenReturn(inter);
        assertNotNull(service.saveInteraction(inter));
    }

    @Test
    void testSave_CasLimite_Exception() {
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Interaction(1L, new Date(), "", TypeInteraction.values()[0]); // Résumé vide = erreur !
        });
    }

    @Test
    void testDelete_VerificationAppel() {
        service.deleteInteraction(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}