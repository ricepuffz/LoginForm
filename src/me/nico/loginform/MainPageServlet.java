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
		
		String logoutTag = request.getParameter("logout");
		
		if (logoutTag != null && logoutTag.equals("true"))
		{
			SessionManager.unauthenticateToken(request.getParameter("username"), request.getParameter("token"));
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		
		if (token == null || username == null || !SessionManager.authenticateToken(username, token))
			response.sendRedirect("LoginServlet");
		else
		{
			String[] html = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/mainPage.html");
			String[] navigationBarHtml = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/navigationBar.html");
			String title = GlobalInfo.userDatabase.getGender(username).equals("Male") ? "Mr. " : "Mrs. ";
			String name = GlobalInfo.userDatabase.getName(username);
			
			out.print(html[0] + request.getContextPath() + html[1] + navigationBarHtml[0] + request.getContextPath() +
					navigationBarHtml[1] + html[2] + title + name + html[3]
					+ username + html[4] + token + html[5]
					+ username + html[6] + token + html[7]
					+ username + html[8] + token + html[9]
					+ username + html[10] + token + html[11]
					+ username + html[12] + token + html[13]
					+ username + html[14] + token + html[15]
					+ username + html[16] + token + html[17]
					+ username + html[18] + token + html[19]
					+ username + html[20] + token + html[21]);
		}
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = request.getParameter("username");
		String token = request.getParameter("token");
		String navSearchInput = request.getParameter("navSearchInput");
		if (navSearchInput == null)
		{
			String searchinput = request.getParameter("searchinput");
			String type = request.getParameter("type");
			
			response.sendRedirect(request.getContextPath() + "/StockServlet?symbol=" + searchinput + "&type=" + type + "&username=" + username + "&token=" + token);
		} else {
			String type = "volume";
			String[] searchParams = navSearchInput.split(":");
			if (searchParams.length > 1)
				type = searchParams[1];
			response.sendRedirect(request.getContextPath() + "/StockServlet?symbol=" + searchParams[0] + "&type=" + type + "&username=" + request.getParameter("username") + "&token=" + request.getParameter("token"));
		}
	}
}
