package adressBook.entités;

public class Contact {
    // Attributs privés 
    private Long id; 
    private String name;
    private String email;
    private String phone;
    private String notes;

    // Constructeur avec validation
    public Contact(Long id, String name, String email, String phone, String notes) {
        // on refuse de créer un contact sans nom
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Erreur : Le nom du contact est obligatoire !");
        }
        
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.notes = notes;
    }

    // Getters pour que les autres couches puissent lire les informations
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getNotes() { return notes; }
}
