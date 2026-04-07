package addressBook.entities;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.Date;

public class InteractionTest {
    
    @Test
    void testCreation_Nominal() {
        
        Interaction inter = new Interaction(1L, new Date(), "Rdv", TypeInteraction.values()[0]);
        assertEquals("Rdv", inter.getSummary());
    }

    @Test
    void testSummary_Null_ThrowsException() {
        
        assertThrows(IllegalArgumentException.class, () -> {
            new Interaction(1L, new Date(), null, TypeInteraction.values()[0]);
        });
    }
}