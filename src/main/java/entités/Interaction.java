package adressBook.entités;

import java.util.Date;

public class Interaction {
    private Long id;
    private Date date;
    private String summary;
    private String type;

    public Interaction(Long id, Date date, String summary, String type) throws IllegalArgumentException {
        if (id == null) {
            throw new IllegalArgumentException("L'ID est obligatoire.");
        }
        if (date == null) {
            throw new IllegalArgumentException("La date est obligatoire.");
        }
        if (summary == null || summary.trim().isEmpty()) {
            throw new IllegalArgumentException("Le résumé ne peut pas être vide.");
        }
        if (type == null || type.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type est obligatoire.");
        }

        this.id = id;
        this.date = date;
        this.summary = summary;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public String getSummary() {
        return summary;
    }

    public String getType() {
        return type;
    }
}