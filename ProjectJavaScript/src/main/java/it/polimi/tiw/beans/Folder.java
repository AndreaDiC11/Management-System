package it.polimi.tiw.beans;

import java.util.List;

public class Folder {
    private String creator;
    private String name;
    private List<Document> documents;
    private List<Folder> folders;
    private int parent_id;
    private int id;

    public Folder() {
    }

    public Folder(int id, String creator, String name, int parent_id) {
    	this.id = id;
        this.creator = creator;
        this.name = name;
        this.parent_id = parent_id;
    }
    

    // Metodi getter e setter

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Document> getDocuments() {
        return documents;
    }

    public void setDocuments(List<Document> documents) {
        this.documents = documents;
    }
    
    public List<Folder> getFolders() {
        return folders;
    }

    public void setFolders(List<Folder> folders) {
        this.folders = folders;
    }
    
    public int getParent_id() {
        return parent_id;
    }

    public void setParent_id(int parent_id) {
        this.parent_id = parent_id;
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
