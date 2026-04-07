package adressBook.entités;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import addressBook.entites.Interaction;

public class InteractionTest {
    @Test
    void testCreation_Nominal() {
        Interaction inter = new Interaction();
        inter.setSummary("Rdv");
        assertEquals("Rdv", inter.getSummary());
    }

    @Test
    void testSummary_Null() {
        Interaction inter = new Interaction();
        inter.setSummary(null);
        assertNull(inter.getSummary());
    }
}