package it.polimi.tiw.dao;

import it.polimi.tiw.beans.User;
import java.sql.*;


public class UserDAO {
    private Connection connection = null;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    // Metodo per aggiungere un utente al database
    public void addUser(User user) throws SQLException {
        String SELECT_USER_SQL = "SELECT * FROM Users WHERE username = ?";
        String INSERT_USER_SQL = "INSERT INTO Users (username, email, password) VALUES (?, ?, ?)";
        String INSERT_TRASH_FOLDER_SQL = "INSERT INTO folders (creator, name, parent_id) VALUES (?, ?, ?)";
        String INSERT_TOP_FOLDER_SQL = "INSERT INTO folders (creator, name, parent_id) VALUES (?, ?, ?)";


        
        try {
            // Verifica se lo username è già presente nel database
            PreparedStatement selectStatement = connection.prepareStatement(SELECT_USER_SQL);
            selectStatement.setString(1, user.getUsername());
            ResultSet resultSet = selectStatement.executeQuery();

            // Se lo username è già presente nel database, genera un'eccezione
            if (resultSet.next()) {
                throw new SQLException("Username already used.");
            }

            // Inserisce l'utente nel database
            PreparedStatement insertStatement = connection.prepareStatement(INSERT_USER_SQL);
            insertStatement.setString(1, user.getUsername());
            insertStatement.setString(2, user.getEmail());
            insertStatement.setString(3, user.getPassword());

            insertStatement.executeUpdate();
            
            // Inserisce la cartella "Cestino" associata all'utente nel database
            PreparedStatement insertTrashFolderStatement = connection.prepareStatement(INSERT_TRASH_FOLDER_SQL);
            insertTrashFolderStatement.setString(1, user.getUsername());
            insertTrashFolderStatement.setString(2, "Cestino");
            insertTrashFolderStatement.setInt(3, 0);


            insertTrashFolderStatement.executeUpdate();
            
            // Inserisce la cartella "Cartella" associata all'utente nel database
            PreparedStatement insertTopFolderStatement = connection.prepareStatement(INSERT_TOP_FOLDER_SQL);
            insertTopFolderStatement.setString(1, user.getUsername());
            insertTopFolderStatement.setString(2, "Cartella");
            insertTopFolderStatement.setInt(3, 0);


            insertTopFolderStatement.executeUpdate();

        } catch (SQLException e) {
            // Gestione dell'eccezione SQLException
            System.err.println("Error while adding user to database.");
            throw e; // Rilancia l'eccezione per consentire la gestione a livello superiore
        }
    }
    
    public User checkCredentials(String username, String password) throws SQLException {
        String query = "SELECT * FROM Users WHERE username = ? AND password = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(resultSet.getString("username"),
                            resultSet.getString("email"), resultSet.getString("password"));
                }
                else {
                    return null;
                }
            }
            catch (SQLException e) {
                // Gestione dell'eccezione SQLException
                System.err.println("Error while adding user to database.");
                throw e; // Rilancia l'eccezione per consentire la gestione a livello superiore
            }
        }
    }
}
