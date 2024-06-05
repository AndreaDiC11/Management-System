package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.DocumentDAO;
import it.polimi.tiw.dao.FolderDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/EliminaElemento")
public class EliminaElemento extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public EliminaElemento() {
        super();
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        if (session.isNew() || session.getAttribute("user") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("User not logged in.");
            return;
        }

        User user = (User) session.getAttribute("user");
        String documentIdStr = request.getParameter("documentId");
        String folderIdStr = request.getParameter("folderId");

        if ((documentIdStr == null || documentIdStr.isEmpty()) && (folderIdStr == null || folderIdStr.isEmpty())) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Invalid request parameters.");
            return;
        }


        else {
        }

        DocumentDAO documentDao = new DocumentDAO(connection);
        FolderDAO folderDao = new FolderDAO(connection);

        try {
            if (documentIdStr != null && !"undefined".equals(documentIdStr)) {
            	int documentId = Integer.parseInt(documentIdStr);
            	
	            if (documentDao.findDocumentById(documentId) != null) {
	                documentDao.deleteDocument(documentId);
	                response.getWriter().write("{\"status\":\"success\"}");
	            }
	            else {
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                response.getWriter().write("Element not found.");
	            }
            } else {         
            	int folderId = Integer.parseInt(folderIdStr);

	            if (folderDao.findFolderById(folderId) != null) {
	                folderDao.deleteFolder(folderId);
	                response.getWriter().write("{\"status\":\"success\"}");
	            } 
	            else {
	                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	                response.getWriter().write("Element not found.");
	            }
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Errore: " + e.getMessage());
        }
    }

    public void destroy() {
        try {
            ConnectionHandler.closeConnection(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
