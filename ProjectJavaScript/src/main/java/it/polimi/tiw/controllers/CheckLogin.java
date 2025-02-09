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

import org.apache.commons.lang.StringEscapeUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;


import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/CheckLogin")
public class CheckLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public CheckLogin() {
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
		
		
		// obtain and escape params
		String name = null;
		String pass = null;
		
		
		try {
			name = StringEscapeUtils.escapeJava(request.getParameter("username"));
			pass = StringEscapeUtils.escapeJava(request.getParameter("pwd"));


			
			if (name == null || pass == null || name.isEmpty() || pass.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
					
		

		} catch (Exception e) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", e.getMessage());
			String path = "/WEB-INF/login.html";
			templateEngine.process(path, ctx, response.getWriter());
	        return;
		}

		// query db to authenticate for user
		UserDAO userDao = new UserDAO(connection);
		try {
            User user = userDao.checkCredentials(name, pass);
            if (user != null) {
			    System.out.println("User present in the database.");
			    ///Dopo aver aggiunto con successo l'utente al database
				request.getSession().setAttribute("user", user);
				String path = getServletContext().getContextPath() + "/HomePage";
				response.sendRedirect(path);
            }
            else {
                throw new SQLException("Invalid Credentials.");
            }
		} catch (SQLException e) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", e.getMessage());
			String path = "/WEB-INF/login.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
	}
}