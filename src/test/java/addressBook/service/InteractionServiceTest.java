package addressBook.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import addressBook.repository.InteractionRepository;
import adressBook.entités.Interaction;

@ExtendWith(MockitoExtension.class)
public class InteractionServiceTest {

    @Mock
    private InteractionRepository repository;

    @InjectMocks
    private InteractionService service;

    @Test
    void testSave_Nominal() {
        Interaction inter = new Interaction();
        inter.setSummary("Appel commercial");
        when(repository.save(any(Interaction.class))).thenReturn(inter);
        assertNotNull(service.saveInteraction(inter));
    }

    @Test
    void testSave_CasLimite_Exception() {
        Interaction inter = new Interaction();
        inter.setSummary("");
        assertThrows(IllegalArgumentException.class, () -> service.saveInteraction(inter));
    }

    @Test
    void testDelete_VerificationAppel() {
        service.deleteInteraction(1L);
        verify(repository, times(1)).deleteById(1L);
    }
}