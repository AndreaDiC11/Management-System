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
            response.sendRedirect(getServletContext().getContextPath() + "/index.html");
            return;
        }

        // Recupera l'utente dalla sessione
        User user = (User) session.getAttribute("user");
        int folderId = Integer.parseInt(request.getParameter("parentId"));
        String documentName = StringEscapeUtils.escapeJava(request.getParameter("documentName"));
        String documentDate = StringEscapeUtils.escapeJava(request.getParameter("documentDate"));
        String documentType = StringEscapeUtils.escapeJava(request.getParameter("documentType"));
        String documentSummary = StringEscapeUtils.escapeJava(request.getParameter("documentSummary"));
        
        try { 
        	if (documentName == "" || documentDate == "" || documentType == "" || documentSummary == "") {
				throw new Exception("Missing Information");
        	}
        }catch (Exception e) {
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
            response.setStatus(HttpServletResponse.SC_OK);
            // Recupera i dati aggiornati delle cartelle e dei documenti
            //Folder parentFolder = folderDao.findFolderById(folderId);
            //List<Folder> folders = parentFolder.getFolders();
            //List<Document> documents = parentFolder.getDocuments();

            // Crea la risposta JSON
            //response.setContentType("application/json");
            //PrintWriter out = response.getWriter();
            //out.print(new Gson().toJson(Map.of("folders", folders)));
            //out.flush();
        } catch (SQLException e) {
            // Gestione dell'errore
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
