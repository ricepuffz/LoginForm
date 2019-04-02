package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/RegexCheckerServlet")
public class RegexCheckerServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	
	private static final String EMAIL_REGEX_STRING = "[A-Za-z0-9\\.\\-\\_]+@[A-Za-z0-9-_]+[\\.][A-Za-z\\.]+";
	private static final String PASSWORD_REGEX_STRING = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
       
    public RegexCheckerServlet()
    {
        super();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		//0:white, 1:red, 2:green
		int checkCode = 0;
		@SuppressWarnings("deprecation")
		String query = URLDecoder.decode(request.getQueryString());
		
		if (query.charAt(5) == 'u')
			checkCode = checkUsername(query.substring(22));
		else
			checkCode = checkPassword(query.substring(22));
		
		String output = "";
		
		switch(checkCode) {
			case 0:
				output = "background-color:white";
				break;
			case 1:
				output = "background-color:#ffaaaa";
				break;
			case 2:
				output = "background-color:#aaffaa";
				break;
		}
		
		out.write(output);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doGet(request, response);
	}
	
	private int checkUsername(String content)
	{
		if (content.length() == 0)
			return 0;
		else if (content.matches(EMAIL_REGEX_STRING))
			return 2;
		else
			return 1;
	}
	
	private int checkPassword(String content)
	{
		if (content.length() == 0)
			return 0;
		else if (content.matches(PASSWORD_REGEX_STRING))
			return 2;
		else
			return 1;
	}
}
