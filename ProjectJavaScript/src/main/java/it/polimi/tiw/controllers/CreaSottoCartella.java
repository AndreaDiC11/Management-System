package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringEscapeUtils;

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
            response.sendRedirect(getServletContext().getContextPath() + "/index.html");
            return;
        }

        // Recupera l'utente dalla sessione
        User user = (User) session.getAttribute("user");
        String parentFolder = StringEscapeUtils.escapeJava(request.getParameter("parentFolder"));
        String nomeSottoCartella = StringEscapeUtils.escapeJava(request.getParameter("folderName"));

        if (nomeSottoCartella == null || nomeSottoCartella.isEmpty() || parentFolder == null || parentFolder.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().println("Errore: Parametri mancanti");
            return;
        }

        FolderDAO folderDao = new FolderDAO(connection);

        try {
            folderDao.addSubfolder(user.getUsername(), parentFolder, nomeSottoCartella);
            response.setContentType("application/json");
            response.getWriter().println("{\"success\": true}");
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Errore: " + e.getMessage());
        }
    }
}
