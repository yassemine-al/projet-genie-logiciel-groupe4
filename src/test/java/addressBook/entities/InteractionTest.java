package addressBook.entities;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class InteractionTest {

    @Test
    void testExceptionsConstructeur() {
        
        assertThrows(IllegalArgumentException.class, () -> new Interaction(1L, null, "Test", TypeInteraction.APPEL), "Doit bloquer une date nulle");
        assertThrows(IllegalArgumentException.class, () -> new Interaction(1L, new Date(), "", TypeInteraction.APPEL), "Doit bloquer un résumé vide");
        assertThrows(IllegalArgumentException.class, () -> new Interaction(1L, new Date(), null, TypeInteraction.APPEL), "Doit bloquer un résumé null");
        assertThrows(IllegalArgumentException.class, () -> new Interaction(1L, new Date(), "Test", null), "Doit bloquer un type nul");
    }

    @Test
    void testExceptionsSetters() {
        Interaction inter = new Interaction(1L, new Date(), "Valide", TypeInteraction.APPEL);
        
        assertThrows(IllegalArgumentException.class, () -> inter.setDate(null));
        assertThrows(IllegalArgumentException.class, () -> inter.setSummary(""));
        assertThrows(IllegalArgumentException.class, () -> inter.setSummary(null)); 
        assertThrows(IllegalArgumentException.class, () -> inter.setType(null));
    }

    @Test
    void testCopieDefensiveDate() {
        Date dateInitiale = new Date(100000L);
        Interaction inter = new Interaction(1L, dateInitiale, "Copie Défensive", TypeInteraction.APPEL);
        
        dateInitiale.setTime(999999999L);
        assertNotEquals(dateInitiale.getTime(), inter.getDate().getTime(), "La copie défensive a échoué !");
    }

    @Test
    void testGettersEtSettersValides() {
        Date date = new Date(100000L);
        Interaction inter = new Interaction(1L, date, "Mon résumé", TypeInteraction.APPEL);

        assertEquals(1L, inter.getId());
        assertEquals("Mon résumé", inter.getSummary());
        assertEquals(TypeInteraction.APPEL, inter.getType());
        assertNotNull(inter.getDate());

        inter.setId(99L);
        inter.setSummary("Résumé modifié");
        
        assertEquals(99L, inter.getId());
        assertEquals("Résumé modifié", inter.getSummary());
    }
}