package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/HelpServlet")
public class HelpServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    public HelpServlet()
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String[] html = HTMLReader.readHtml("C:/Users/Nico/Desktop/workspace/LoginForm/WebContent/helpPage.html");
		out.print(html[0]);
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}

}
