package it.polimi.tiw.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import it.polimi.tiw.beans.User;
import it.polimi.tiw.dao.UserDAO;
import it.polimi.tiw.utils.ConnectionHandler;

@WebServlet("/CheckRegister")
public class CheckRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Connection connection = null;
	private TemplateEngine templateEngine;

	public CheckRegister() {
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
		String email = null;
		String pass = null;
		String conf_pass = null;
		
		
		try {
			name = StringEscapeUtils.escapeJava(request.getParameter("username"));
			email = StringEscapeUtils.escapeJava(request.getParameter("email"));
			pass = StringEscapeUtils.escapeJava(request.getParameter("pwd"));
			conf_pass = StringEscapeUtils.escapeJava(request.getParameter("confirmPwd"));


			
			if (name == null || pass == null || name.isEmpty() || pass.isEmpty()) {
				throw new Exception("Missing or empty credential value");
			}
			
			if (!pass.equals(conf_pass)) {
				throw new Exception("Confirm Password is different from Password");
			}
			
			 // Definisci il pattern per un indirizzo email
	        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

	        // Crea il pattern
	        Pattern pattern = Pattern.compile(emailRegex);

	        // Crea un matcher per il pattern
	        Matcher matcher = pattern.matcher(email);

	        // Verifica se l'indirizzo email corrisponde al pattern
	        if (!matcher.matches()) {
	        	throw new Exception("Invalid Email");
	        }
		

		} catch (Exception e) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", e.getMessage());
			String path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
	        return;
		}

		// query db to authenticate for user
		User user = new User(name, email, pass);
		UserDAO userDao = new UserDAO(connection);
		try {
			userDao.addUser(user);
		    System.out.println("User successfully added to the database.");
		    ///Dopo aver aggiunto con successo l'utente al database
			request.getSession().setAttribute("user", user);
			String path = getServletContext().getContextPath() + "/HomePage";
			response.sendRedirect(path);
		} catch (SQLException e) {
			ServletContext servletContext = getServletContext();
			final WebContext ctx = new WebContext(request, response, servletContext, request.getLocale());
			ctx.setVariable("errorMsg", e.getMessage());
			String path = "/index.html";
			templateEngine.process(path, ctx, response.getWriter());
			return;
		}
	}
}