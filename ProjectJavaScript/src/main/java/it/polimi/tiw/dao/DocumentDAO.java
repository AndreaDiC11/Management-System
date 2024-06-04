package it.polimi.tiw.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import it.polimi.tiw.beans.Document;

public class DocumentDAO {
    private final Connection connection;

    public DocumentDAO(Connection connection) {
        this.connection = connection;
    }
    public boolean documentExists(String creator, String documentName) throws SQLException {
        String query = "SELECT COUNT(*) FROM documents WHERE creator = ? AND name = ?";
        boolean exists = false;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, creator);
            statement.setString(2, documentName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    exists = (count > 0);
                }
            }
        }

        return exists;
    }

    
    public void newDocument(String creator, int folderId, String documentName, String documentDate, String documentType, String documentSummary) throws SQLException {
        if (documentExists(creator, documentName)) {
            throw new SQLException("Document already exists for user: " + creator + ", document name: " + documentName);
        }
        String query = "INSERT INTO documents (folder_id, creator, name, date, summary, type) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, folderId);
            statement.setString(2, creator);
            statement.setString(3, documentName);
            statement.setString(4, documentDate);
            statement.setString(5, documentSummary);
            statement.setString(6, documentType);

            statement.executeUpdate();
        }
    }


    public Document findDocumentById(int documentId) throws SQLException {
        String documentQuery = "SELECT * FROM documents WHERE id = ?";
        Document document = null;

        try (PreparedStatement documentStatement = connection.prepareStatement(documentQuery)) {
            documentStatement.setInt(1, documentId);

            try (ResultSet documentResultSet = documentStatement.executeQuery()) {
                if (documentResultSet.next()) {
                    String documentCreator = documentResultSet.getString("creator");
                    String documentName = documentResultSet.getString("name");
                    String documentDate = documentResultSet.getString("date");
                    String documentSummary = documentResultSet.getString("summary");
                    String documentType = documentResultSet.getString("type");
                    int folderId = documentResultSet.getInt("folder_id");

                    document = new Document(documentId, folderId, documentCreator, documentName, documentDate, documentSummary, documentType);
                }
            }
        }

        return document;
    }
    public void updateDocument(int documentId, int destinationFolderId) throws SQLException {
    	
        String moveQuery = "UPDATE documents SET folder_id = ? WHERE id = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(moveQuery)) {
            statement.setInt(1, destinationFolderId);
            statement.setInt(2, documentId);

            statement.executeUpdate();
        }
    }
    public Document findDocumentByFolderIdAndName(int folderId, String documentName) throws SQLException {
        String query = "SELECT * FROM documents WHERE folder_id = ? AND name = ?";
        Document document = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, folderId);
            statement.setString(2, documentName);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int documentId = resultSet.getInt("id");
                    String documentCreator = resultSet.getString("creator");
                    String documentDate = resultSet.getString("date");
                    String documentSummary = resultSet.getString("summary");
                    String documentType = resultSet.getString("type");

                    document = new Document(documentId, folderId, documentCreator, documentName, documentDate, documentSummary, documentType);
                }
            }
        }

        return document;
    }
}
