package adressBook.entités;

import java.util.Date;

public class Interaction {
    private Long id;
    private Date date;
    private String summary;
    private TypeInteraction type; 

    public Interaction() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSummary() { return summary; }
    public void setSummary(String summary) { this.summary = summary; }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }

    public TypeInteraction getType() { return type; }
    public void setType(TypeInteraction type) { this.type = type; }
}