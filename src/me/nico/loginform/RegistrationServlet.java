package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sun.security.action.GetLongAction;

@WebServlet("/RegistrationServlet")
public class RegistrationServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
       
    public RegistrationServlet()
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		
		String[] html = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/registrationPage.html");
		
		out.print(html[0] + request.getContextPath() + html[1]);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter("usernameinput");
		String password = request.getParameter("passwordinput");
		String gender = request.getParameter("genderselect");
		String name = request.getParameter("nameinput");
		String firstName = request.getParameter("firstnameinput");
		
		if (GlobalInfo.userDatabase == null)
			try {
				GlobalInfo.userDatabase = new UserDatabase();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		GlobalInfo.userDatabase.register(username, password, gender, name, firstName);
	}
}
