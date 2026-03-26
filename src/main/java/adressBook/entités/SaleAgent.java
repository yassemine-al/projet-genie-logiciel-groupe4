package adressBook.entités;

public class SaleAgent {
  
    private Long id;
    private String username;
    private String password;

    public SaleAgent(Long id, String username, String password) {
        if (id == null) {
            throw new IllegalArgumentException("L'ID est obligatoire"); 
        }
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom d'utilisateur ne peut pas être vide");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("Le mot de passe doit contenir au moins 4 caractères"); 
        }
        
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
    public boolean checkPassword(String attempt) {
        return this.password.equals(attempt);
    }
}