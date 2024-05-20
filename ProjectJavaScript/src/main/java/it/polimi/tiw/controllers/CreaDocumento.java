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
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

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
        String folderName = StringEscapeUtils.escapeJava(request.getParameter("folderName"));
        String documentName = StringEscapeUtils.escapeJava(request.getParameter("documentName"));
        String documentDate = StringEscapeUtils.escapeJava(request.getParameter("documentDate"));
        String documentType = StringEscapeUtils.escapeJava(request.getParameter("documentType"));
        String documentSummary = StringEscapeUtils.escapeJava(request.getParameter("documentSummary"));

        
        try { 
        	if (folderName == "" || documentName == "" || documentDate == "" || documentType == "" || documentSummary == "") {
				throw new Exception("Missing Information");
        	}
        }catch (Exception e) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg3", e.getMessage());
			String path = "/WEB-INF/GestioneContenuti.html";
			templateEngine.process(path, ctx, response.getWriter());
	        return;
        }


        DocumentDAO documentDao = new DocumentDAO(connection);
        FolderDAO folderDao = new FolderDAO(connection);
        Folder folder = null;
		try {
			folder = folderDao.findFolderByName(user.getUsername(), folderName);
			if (folder == null) {
	            throw new SQLException("La cartella genitore non esiste");
			}
		} catch (SQLException e) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg3", e.getMessage());
			String path = "/WEB-INF/GestioneContenuti.html";
			templateEngine.process(path, ctx, response.getWriter());
	        return;
		}

        try {
        	documentDao.newDocument(user.getUsername(), folder.getId(), documentName, documentDate, documentType, documentSummary);

            response.sendRedirect(request.getContextPath() + "/GestioneContenuti");

        } catch (SQLException e) {
            // Gestione dell'errore
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg1", e.getMessage());
			String path = "/WEB-INF/GestioneContenuti.html";
			templateEngine.process(path, ctx, response.getWriter());
	        return;
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
