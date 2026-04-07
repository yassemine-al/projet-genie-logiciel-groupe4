package addressBook.entities;

import java.util.Date;

public class Interaction {
    private Long id;
    private Date date;
    private String summary;
    private TypeInteraction type; 

    // Constructeur strict : on force à donner les infos dès la création
    public Interaction(Long id, Date date, String summary, TypeInteraction type) {
        if (summary == null || summary.isEmpty()) {
            throw new IllegalArgumentException("Erreur : Le résumé est obligatoire !");
        }
        if (type == null) {
            throw new IllegalArgumentException("Erreur : Le type d'interaction est obligatoire !");
        }
        
        this.id = id;
        this.date = date;
        this.summary = summary;
        this.type = type;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public TypeInteraction getType() { return type; }
    public void setType(TypeInteraction type) { this.type = type; }
}