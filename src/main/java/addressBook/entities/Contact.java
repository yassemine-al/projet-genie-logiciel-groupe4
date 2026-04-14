package addressBook.entities;

import java.util.HashSet;
import java.util.Set;

public class Contact {
    private Long id; 
    private String name;
    private String email;
    private String phone;
    private String notes;
    private Long agentId;
    private Set<Category> categories = new HashSet<>();
    public Contact(Long id, String name, String email, String phone, String notes,Long agentId) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Le nom du contact est obligatoire !");
        }
        if (agentId == null) {
            throw new IllegalArgumentException("Erreur : L'ID de l'agent est obligatoire !");
        }
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.notes = notes;
        this.agentId=agentId;
    }
    public Long getAgentId() { return agentId; }
    public void setAgentId(Long agentId) { this.agentId = agentId; }

    // --- LA COPIE DÉFENSIVE EST LÀ ! ---
    public Set<Category> getCategories() { 
        return new HashSet<>(this.categories); // On renvoie une copie sécurisée !
    }

    public void addCategory(Category category) {
        if (category != null) {
            this.categories.add(category);
        }
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getNotes() { return notes; }

    public void setId(Long id) { this.id = id; }

    public void setName(String name) { 
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Le nom du contact est obligatoire !");
        }
        this.name = name; 
    }
    
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setNotes(String notes) { this.notes = notes; }
}