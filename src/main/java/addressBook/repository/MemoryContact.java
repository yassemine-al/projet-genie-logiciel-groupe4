package addressBook.repository;

import adressBook.entités.Contact;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MemoryContact implements ContactRepository {
    
    private final String filePath;
    private final Gson gson;

    // Constructeur : on lui donne le nom du fichier dans lequel on veut sauvegarder
    public MemoryContact(String filePath) {
        this.filePath = filePath;
        this.gson = new Gson();
    }

    @Override
    public List<Contact> findAll() {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>(); // Si le fichier n'existe pas, on renvoie une liste vide
        }
        
        try (Reader reader = new FileReader(filePath)) {
            Type listType = new TypeToken<ArrayList<Contact>>(){}.getType();
            List<Contact> contacts = gson.fromJson(reader, listType);
            return contacts != null ? contacts : new ArrayList<>();
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    @Override
    public Contact save(Contact contact) {
        List<Contact> contacts = findAll();
        contacts.add(contact);
        
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(contacts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contact;
    }

    @Override
    public Optional<Contact> findById(Long id) {
        return findAll().stream().filter(c -> c.getId().equals(id)).findFirst();
    }

    @Override
    public void delete(Long id) {
        List<Contact> contacts = findAll();
        contacts.removeIf(c -> c.getId().equals(id));
        
        try (Writer writer = new FileWriter(filePath)) {
            gson.toJson(contacts, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}