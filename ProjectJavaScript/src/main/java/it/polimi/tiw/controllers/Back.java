
package it.polimi.tiw.controllers;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/Back")
public class Back extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	String uri = request.getParameter("oldUrl");
    	
        if (uri == null || uri.isEmpty()) {
            // Se l'URI è vuoto o null, reindirizza a index
            response.sendRedirect(request.getContextPath() + "/index.html");
            return;
        }


        if (uri.equalsIgnoreCase("/Project/AccessDocument")) {
        	String folderId = request.getParameter("folderId");
    		String previousPage = "/Project/Contenuti?folderId=" + folderId  ;
            response.sendRedirect(previousPage);
        }
        else if(uri.equalsIgnoreCase("/Project/MoveDocument")) {
        	String folderId = request.getParameter("folderId");
    		String previousPage = "/Project/Contenuti?folderId=" + folderId  ;
            response.sendRedirect(previousPage);
        }
        else {
    		String previousPage = "/Project/HomePage";
            response.sendRedirect(previousPage);
        }
    }
}
