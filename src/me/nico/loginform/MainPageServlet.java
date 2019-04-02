package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/MainPageServlet")
public class MainPageServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    public MainPageServlet()
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String token = request.getParameter("token");
		String username = request.getParameter("username");
		
		if (token == null || username == null || !SessionManager.authenticateToken(username, token))
			response.sendRedirect("LoginServlet");
		else
		{
			String[] html = HTMLReader.readHtml("C:/Users/Nico/Desktop/workspace/LoginForm/WebContent/mainPage.html");
			
			out.print(html[0]);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String searchinput = request.getParameter("searchinput");
		String type = request.getParameter("type");
		String token = request.getParameter("token");
		String username = request.getParameter("username");
		
		response.sendRedirect("/LoginForm/StockServlet?symbol=" + searchinput + "&type=" + type + "&username=" + username + "&token=" + token);
	}

}
