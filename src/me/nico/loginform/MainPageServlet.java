package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
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
		
		//[0]: isVerified, [1]: username, [2]: token, [3]: response
		Object[] verificationCheck = verificationCheck(request, response);
		
		if (!(boolean) verificationCheck[0])
		{
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		
		//response = (HttpServletResponse) verificationCheck[3];
		
		String username = (String) verificationCheck[1];
		String token = (String) verificationCheck[2];
		
		String logoutTag = request.getParameter("logout");
		
		if (logoutTag != null && logoutTag.equals("true"))
		{
			SessionManager.unauthenticateToken(username, token);
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		
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
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String username = "";
		String token = "";
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals("username"))
					username = cookie.getValue();
				else if (cookie.getName().equals("token"))
					token = cookie.getValue();
			}
		}
		
		if (username == "" || token == "")
		{
			username = request.getParameter("username");
			token = request.getParameter("token");
		}
		
		if (!(username == null) && !(token == null))
		{
			String navSearchInput = request.getParameter("navSearchInput");
			if (navSearchInput == null)
			{
				String searchinput = request.getParameter("searchinput");
				String type = request.getParameter("type");
				
				response.sendRedirect(request.getContextPath() + "/StockServlet?symbol=" + searchinput + "&type=" + type + "&username=" + username + "&token=" + token);
				return;
			} else {
				String type = "volume";
				String[] searchParams = navSearchInput.split(":");
				
				if (searchParams.length > 1)
					type = searchParams[1];
				
				response.sendRedirect(request.getContextPath() + "/StockServlet?symbol=" + searchParams[0] + "&type=" + type + "&username=" + request.getParameter("username") + "&token=" + request.getParameter("token"));
				return;
			}
		}
	}
	
	
	private Object[] verificationCheck(HttpServletRequest request, HttpServletResponse response)
	{
		String username = "";
		String token = "";
		
		Cookie usernameCookie = null;
		Cookie tokenCookie = null;
		
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
		{
			for (Cookie cookie : cookies)
			{
				if (cookie.getName().equals("username"))
				{
					username = cookie.getValue();
					usernameCookie = cookie;
				}
				else if (cookie.getName().equals("token"))
				{
					token = cookie.getValue();
					tokenCookie = cookie;
				}
			}
		
			if (username != "" && token != "")
			{
				if (SessionManager.authenticateToken(username, token))
					return new Object[] { true, username, token, response };
				else
				{
					usernameCookie.setMaxAge(0);
					tokenCookie.setMaxAge(0);
					response.addCookie(usernameCookie);
					response.addCookie(tokenCookie);
					return new Object[] { false, username, token, response };
				}
			}
		}
		
		username = request.getParameter("username");
		token = request.getParameter("token");
		
		if (token == null || username == null || !SessionManager.authenticateToken(username, token))
			return new Object[] { false, null, null, response };
		return new Object[] { true, username, token, response };
	}
}
