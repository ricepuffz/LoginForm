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
			String[] html = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/mainPage.html");
			String[] navigationBarHtml = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/navigationBar.html");
			
			out.print(html[0] + request.getContextPath() + html[1] + navigationBarHtml[0] + request.getContextPath() + navigationBarHtml[1] + html[2]);
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
			
			response.sendRedirect("/LoginForm/StockServlet?symbol=" + searchinput + "&type=" + type + "&username=" + username + "&token=" + token);
		} else {
			String type = "volume";
			String[] searchParams = navSearchInput.split(":");
			if (searchParams.length > 1)
				type = searchParams[1];
			response.sendRedirect(request.getContextPath() + "/StockServlet?symbol=" + searchParams[0] + "&type=" + type + "&username=" + request.getParameter("username") + "&token=" + request.getParameter("token"));
		}
	}
}
