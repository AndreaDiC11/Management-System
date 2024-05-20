package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polimi.tiw.beans.Document;
import it.polimi.tiw.beans.Folder;

public class FolderDAO {
    private final Connection connection;

    public FolderDAO(Connection connection) {
        this.connection = connection;
    }
    
    public Folder findFolderByName(String creator, String folderName) throws SQLException {
        String query = "SELECT * FROM folders WHERE creator = ? AND name = ?";
        Folder folder = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, creator);
            statement.setString(2, folderName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int folderId = resultSet.getInt("id");
                    int parentId = resultSet.getInt("parent_id");
                    folder = new Folder(folderId, creator, folderName, parentId);
                }
            }
        }

        return folder;
    }

    
    private boolean folderExists(String username, String folderName) throws SQLException {
        String query = "SELECT COUNT(*) AS count FROM folders WHERE creator = ? AND name = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, folderName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt("count");
                    return count > 0;
                }
            }
        }
        return false;
    }
    
    public void newFolder(String username, String folderName) throws SQLException {
        if (folderExists(username, folderName)) {
            throw new SQLException("Folder already exists for user: " + username + ", folder name: " + folderName);
        }
        String query = "INSERT INTO folders (creator, name, parent_id) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, username);
            statement.setString(2, folderName);
            statement.setInt(3, 0);

            statement.executeUpdate();
        }
    }
    
    public void addSubfolder(String creator, int parentId, String newFolderName) throws SQLException {
        // Ottieni l'id della cartella genitore
        Folder parent = findFolderById(parentId);
        if (parent == null) {
            throw new SQLException("La cartella genitore non esiste");
        }
        if (folderExists(creator, newFolderName)) {
            throw new SQLException("Folder already exists for user: " + creator + ", folder name: " + newFolderName);
        }

        // Inserisci la nuova cartella come sottocartella
        String query = "INSERT INTO folders (name, parent_id, creator) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newFolderName);
            statement.setInt(2, parentId);
            statement.setString(3, creator); // Imposta il creator come necessario

            statement.executeUpdate();
        }
    }
    
    public Folder findTopFolder(String creator, String folderName) throws SQLException {
        String query = "SELECT * FROM folders WHERE creator = ? AND name = ? AND parent_id = 0";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, creator);
            statement.setString(2, folderName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int folderId = resultSet.getInt("id");
                    return new Folder(folderId, creator, folderName, 0);
                }
            }
        }
        return null; // Ritorna null se la cartella top non è stata trovata
    }

    public List<Folder> findFoldersByUser(String creator) throws SQLException {
        List<Folder> folders = new ArrayList<>();
        String folderQuery = "SELECT * FROM folders WHERE creator = ? AND parent_id = ?";

        try (PreparedStatement folderStatement = connection.prepareStatement(folderQuery)) {
            folderStatement.setString(1, creator);
            folderStatement.setInt(2, 0);


            try (ResultSet folderResultSet = folderStatement.executeQuery()) {
                while (folderResultSet.next()) {
                    String folderCreator = folderResultSet.getString("creator");
                    String folderName = folderResultSet.getString("name");
                    int folderId = folderResultSet.getInt("id");

                    Folder folder = new Folder(folderId, folderCreator, folderName, 0);

                    // Ottieni le cartelle nidificate
                    List<Folder> subfolders = findSubfolders(folderId);
                    folder.setFolders(subfolders);

                    // Ottieni i documenti associati alla cartella corrente
                    List<Document> documents = findDocumentsInFolder(folderId);
                    folder.setDocuments(documents);

                    // Aggiungi la cartella alla lista delle cartelle
                    folders.add(folder);
                }
            }
        }

        return folders;
    }

    private List<Folder> findSubfolders(int parentId) throws SQLException {
        List<Folder> subfolders = new ArrayList<>();
        String subfolderQuery = "SELECT * FROM folders WHERE parent_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(subfolderQuery)) {
            statement.setInt(1, parentId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String folderCreator = resultSet.getString("creator");
                    String folderName = resultSet.getString("name");
                    int folderId = resultSet.getInt("id");


                    Folder subfolder = new Folder(folderId ,folderCreator, folderName, parentId);

                    // Ricorsivamente ottieni le cartelle nidificate
                    List<Folder> nestedSubfolders = findSubfolders(folderId);
                    subfolder.setFolders(nestedSubfolders);

                    // Ottieni i documenti associati alla cartella corrente
                    List<Document> documents = findDocumentsInFolder(folderId);
                    subfolder.setDocuments(documents);

                    subfolders.add(subfolder);
                }
            }
        }

        return subfolders;
    }

    private List<Document> findDocumentsInFolder(int folderId) throws SQLException {
        List<Document> documents = new ArrayList<>();
        String documentQuery = "SELECT * FROM documents WHERE folder_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(documentQuery)) {
            statement.setInt(1, folderId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                	int documentId = resultSet.getInt("id");
                	String documentCreator = resultSet.getString("creator");
                    String documentName = resultSet.getString("name");
                    String documentDate = resultSet.getString("date");
                    String documentSummary = resultSet.getString("summary");
                    String documentType = resultSet.getString("type");

                    Document document = new Document(documentId, folderId, documentCreator, documentName, documentDate, documentSummary, documentType);
                    documents.add(document);
                }
            }
        }

        return documents;
    }
    
    public Folder findFolderById(int folderId) throws SQLException {
        String folderQuery = "SELECT * FROM folders WHERE id = ?";
        Folder folder = null;

        try (PreparedStatement folderStatement = connection.prepareStatement(folderQuery)) {
            folderStatement.setInt(1, folderId);

            try (ResultSet folderResultSet = folderStatement.executeQuery()) {
                if (folderResultSet.next()) {
                    String folderCreator = folderResultSet.getString("creator");
                    String folderName = folderResultSet.getString("name");
                    int parentId = folderResultSet.getInt("parent_Id");


                    folder = new Folder(folderId, folderCreator, folderName, parentId);
                    
                    List<Folder> subfolders = findSubfolders(folderId);
                    folder.setFolders(subfolders);

                    // Ottieni i documenti associati alla cartella corrente
                    List<Document> documents = findDocumentsInFolder(folderId);
                    folder.setDocuments(documents);

                }
            }
        }

        return folder;
    }

}