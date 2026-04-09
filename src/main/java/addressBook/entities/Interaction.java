package addressBook.entities;

import java.util.Date;

public class Interaction {
    private Long id;
    private Date date;
    private String summary;
    private TypeInteraction type; 

  
    public Interaction(Long id, Date date, String summary, TypeInteraction type) {
        if (date == null) {
            throw new IllegalArgumentException("Erreur : La date est obligatoire !");
        }
        if (summary == null || summary.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Le résumé est obligatoire !");
        }
        if (type == null) {
            throw new IllegalArgumentException("Erreur : Le type d'interaction est obligatoire !");
        }
        
        this.id = id;
        this.date = new Date(date.getTime()); 
        this.summary = summary;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSummary() { return summary; }
    
    public void setSummary(String summary) { 
        if (summary == null || summary.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Le résumé est obligatoire !");
        }
        this.summary = summary; 
    }

    public Date getDate() { 
        return new Date(this.date.getTime()); // Copie défensive !
    }
    
    public void setDate(Date date) { 
        if (date == null) {
            throw new IllegalArgumentException("Erreur : La date est obligatoire !");
        }
        this.date = new Date(date.getTime()); 
    }

    public TypeInteraction getType() { return type; }
    
    public void setType(TypeInteraction type) { 
        if (type == null) {
            throw new IllegalArgumentException("Erreur : Le type d'interaction est obligatoire !");
        }
        this.type = type; 
    }
}