package addressBook.entities;

import org.junit.jupiter.api.Test;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;

class InteractionTest {

    @Test
    void testExceptionsConstructeur() {
        assertThrows(IllegalArgumentException.class, () -> new Interaction(1L, null, "Test", null), "Doit bloquer une date nulle");
        assertThrows(IllegalArgumentException.class, () -> new Interaction(1L, new Date(), "", null), "Doit bloquer un résumé vide");
        assertThrows(IllegalArgumentException.class, () -> new Interaction(1L, new Date(), "Test", null), "Doit bloquer un type nul");
    }

    @Test
    void testExceptionsSetters() {
     
        Interaction inter = new Interaction(1L, new Date(), "Valide", null); 
        
  
        assertThrows(IllegalArgumentException.class, () -> inter.setDate(null));
        assertThrows(IllegalArgumentException.class, () -> inter.setSummary(""));
        assertThrows(IllegalArgumentException.class, () -> inter.setType(null));
    }

    @Test
    void testCopieDefensiveDate() {
        Date dateInitiale = new Date(100000L);
        Interaction inter = new Interaction(1L, dateInitiale, "Copie Défensive", null);
        
        dateInitiale.setTime(999999999L);
        assertNotEquals(dateInitiale.getTime(), inter.getDate().getTime(), "La copie défensive a échoué !");
    }
}