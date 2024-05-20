package it.polimi.tiw.beans;

public class Document {
	private int id;
	private int folder_id;
    private String creator;
    private String name;
    private String date;
    private String summary;
    private String type;
    private boolean open;

    public Document() {
    }

    public Document(int id, int folder_id, String creator, String name, String date, String summary, String type) {
    	this.id = id;
    	this.folder_id = folder_id;
        this.creator = creator;
        this.name = name;
        this.date = date;
        this.summary = summary;
        this.type = type;
        this.open = false;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public int getId() {
    	return id;
    }
    
    public int getFolder_id() {
    	return folder_id;
    }
    
    public void setFolder_id(int folder_id) {
    	this.folder_id = folder_id;
    }

	public boolean isOpen() {
		return open;
	}

	public void setOpen(boolean open) {
		this.open = open;
	}
    
    
    
}
