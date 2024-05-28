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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

import com.google.gson.Gson;

import it.polimi.tiw.beans.Folder;
import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.DocumentDAO;
import it.polimi.tiw.dao.FolderDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/CreaDocumento")
public class CreaDocumento extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private Connection connection = null;
    private TemplateEngine templateEngine;

    public CreaDocumento() {
        super();
    }

    public void init() throws ServletException {
        connection = ConnectionHandler.getConnection(getServletContext());
        ServletContext servletContext = getServletContext();
        ServletContextTemplateResolver templateResolver = new ServletContextTemplateResolver(servletContext);
        templateResolver.setTemplateMode(TemplateMode.HTML);
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
        templateResolver.setSuffix(".html");
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
        int folderId = Integer.parseInt(request.getParameter("parentId"));
        String documentName = StringEscapeUtils.escapeJava(request.getParameter("documentName"));
        String documentDate = StringEscapeUtils.escapeJava(request.getParameter("documentDate"));
        String documentType = StringEscapeUtils.escapeJava(request.getParameter("documentType"));
        String documentSummary = StringEscapeUtils.escapeJava(request.getParameter("documentSummary"));
        
        try { 
            if (documentName.isEmpty() || documentDate.isEmpty() || documentType.isEmpty() || documentSummary.isEmpty()) {
                throw new Exception("Missing Information");
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Errore: " + e.getMessage());
            return;
        }

        DocumentDAO documentDao = new DocumentDAO(connection);
        FolderDAO folderDao = new FolderDAO(connection);
        Folder folder = null;
        try {
            folder = folderDao.findFolderById(folderId);
            if (folder == null) {
                throw new SQLException("La cartella genitore non esiste");
            }
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("Errore: " + e.getMessage());
            return;
        }

        try {
            documentDao.newDocument(user.getUsername(), folderId, documentName, documentDate, documentType, documentSummary);
            int documentId = documentDao.findDocumentByFolderIdAndName(folderId, documentName).getId();
            //response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write("{\"status\":\"success\", \"documentId\": " + documentId + "}");
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
