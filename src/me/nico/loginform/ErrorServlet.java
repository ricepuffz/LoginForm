package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/ErrorServlet")
public class ErrorServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    public ErrorServlet() 
   {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String message = request.getParameter("message") != null ? request.getParameter("message") : "No Error Message specified";
		
		out.print("<html>"
				+ "<link rel='stylesheet' type='text/css' href='" + request.getContextPath() + "/styles/errorPage.css' />"
				+ "<head>"
				+ "<title>Error</title>"
				+ "</head>"
				+ "<body>"
				+ "<center>"
				+ "<div id=\"container\">"
				+ "<h2>Error</h2>"
				+ "<br>"
				+ "<p>" + message + "</p>"
				+ "<a href=\"" + "MainPageServlet?username=" + request.getParameter("username") + "&token=" + request.getParameter("token") + "\">Return to Main Page</a>"
				+ "</div>"
				+ "</center>"
				+ "</body>"
				+ "</html>");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
