package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	UserDatabase userDatabase = null;
       
    public LoginServlet()
    {
        super();
        
        try {
			userDatabase = new UserDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		
		String[] html = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/loginPage.html");
		
		out.print(html[0] + request.getContextPath() + html[1]);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
		PrintWriter out = response.getWriter();

		boolean verified = userDatabase.verify(request.getParameter("usernameinput"), request.getParameter("passwordinput"));
		if (verified)
			response.sendRedirect("/LoginForm/MainPageServlet?username=" + request.getParameter("usernameinput") + "&token=" + SessionManager.generateToken(request.getParameter("usernameinput")));
		else
			out.print("<br><br>"
					+ "<center>"
					+ "<p style=\"color: red;\">Incorrect username/password!</p>"
					+ "</center>");
	}

}
