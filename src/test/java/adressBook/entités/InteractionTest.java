package adressBook.entités;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class InteractionTest {

    @Test
    void testInteractionNominal() {
        Interaction interaction = new Interaction(1L, new Date(), "Appel client", TypeInteraction.APPEL);
        
        assertNotNull(interaction);
        assertEquals(1L, interaction.getId());
        assertEquals("Appel client", interaction.getSummary());
        assertEquals(TypeInteraction.APPEL, interaction.getType());
    }

    @Test
    void testInteractionIdNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Interaction(null, new Date(), "Appel client", TypeInteraction.APPEL);
        });
        assertEquals("L'ID est obligatoire.", exception.getMessage());
    }

    @Test
    void testInteractionDateNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Interaction(1L, null, "Appel client", TypeInteraction.APPEL);
        });
        assertEquals("La date est obligatoire.", exception.getMessage());
    }

    @Test
    void testInteractionSummaryVide() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Interaction(1L, new Date(), "   ", TypeInteraction.APPEL);
        });
        assertEquals("Le résumé ne peut pas être vide.", exception.getMessage());
    }

    @Test
    void testInteractionTypeNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Interaction(1L, new Date(), "Appel client", null);
        });
        assertEquals("Le type est obligatoire.", exception.getMessage());
    }
}