package it.polimi.tiw.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

import com.google.gson.Gson;

import it.polimi.tiw.beans.Folder;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.FolderDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/CreaSottoCartella")
public class CreaSottoCartella extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;

    public CreaSottoCartella() {
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
        int parentId = Integer.parseInt(request.getParameter("parentId"));
        String nomeSottoCartella = StringEscapeUtils.escapeJava(request.getParameter("folderName"));

        FolderDAO folderDao = new FolderDAO(connection);
        
        try { 
            if (nomeSottoCartella.isEmpty()) {
                throw new Exception("Nome della sottocartella mancante.");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Errore: " + e.getMessage());
            return;
        }

        try {
            folderDao.addSubfolder(user.getUsername(), parentId, nomeSottoCartella);
            //response.setStatus(HttpServletResponse.SC_OK);
            int folderId = folderDao.findFolderByName(user.getUsername(), nomeSottoCartella).getId();
            //response.setContentType("application/json");
            response.getWriter().write("{\"status\":\"success\", \"folderId\": " + folderId + "}");
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
