package adressBook.entités;

import org.junit.jupiter.api.Test;

import addressBook.entites.SaleAgent;

import static org.junit.jupiter.api.Assertions.*;

class SaleAgentTest {

    @Test
    void testConstructor_ValidData() {
        // Cas nominal
        SaleAgent agent = new SaleAgent(1L, "admin", "secret123");
        // Assert
        assertEquals(1L, agent.getId());
        assertEquals("admin", agent.getUsername());
    }

    @Test
    void testConstructor_NullId_ShouldThrowException() {
        // Cas limite : ID null
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SaleAgent(null, "admin", "password");
        });
        assertEquals("L'ID est obligatoire", exception.getMessage());
    }

    @Test
    void testConstructor_EmptyUsername_ShouldThrowException() {
        // Cas limite : Nom d'utilisateur vide
        assertThrows(IllegalArgumentException.class, () -> {
            new SaleAgent(1L, "  ", "password");
        });
    }

    @Test
    void testConstructor_ShortPassword_ShouldThrowException() {
        // Règle métier : Mot de passe trop court (< 4 caractères)
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            new SaleAgent(1L, "user", "123");
        });
        assertTrue(exception.getMessage().contains("au moins 4 caractères"));
    }

    @Test
    void testCheckPassword_Success() {
        SaleAgent agent = new SaleAgent(1L, "user", "password");
        
        // Test de la méthode de vérification
        assertTrue(agent.checkPassword("password"), "Le mot de passe devrait être correct");
        assertFalse(agent.checkPassword("wrong"), "Le mot de passe devrait être refusé");
    }
}