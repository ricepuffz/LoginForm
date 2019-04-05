package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    public LoginServlet()
    {
        super();
        
        try {
			GlobalInfo.userDatabase = new UserDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String cookieUsername = "";
		String cookieToken = "";
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals("username"))
					cookieUsername = cookie.getValue();
				else if (cookie.getName().equals("token"))
					cookieToken = cookie.getValue();
			}
			
			if (cookieUsername != "" && cookieToken != "")
				response.sendRedirect("/LoginForm/MainPageServlet");
		}
		
		PrintWriter out = response.getWriter();
		
		String[] html = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/loginPage.html");
		
		out.print(html[0] + request.getContextPath() + html[1]);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
		PrintWriter out = response.getWriter();

		boolean verified = GlobalInfo.userDatabase.verify(request.getParameter("usernameinput"), request.getParameter("passwordinput"));
		if (verified)
		{
			String token = SessionManager.generateToken(request.getParameter("usernameinput"));
			Cookie usernameCookie = new Cookie("username", request.getParameter("usernameinput"));
			Cookie tokenCookie = new Cookie("token", token);
			response.addCookie(usernameCookie);
			response.addCookie(tokenCookie);
			response.sendRedirect("/LoginForm/MainPageServlet?username=" + request.getParameter("usernameinput") + "&token=" + token);
		}
		else
			out.print("<br><br>"
					+ "<center>"
					+ "<p style=\"color: red;\">Incorrect username/password!</p>"
					+ "</center>");
	}

}
