package me.nico.loginform;

import java.util.HashMap;
import java.util.Map;

public class SessionManager
{
	static String AlphaNumericString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
            						 + "0123456789"
            						 + "abcdefghijklmnopqrstuvxyz"; 
	
	static Map<String, String> tokens = new HashMap<String, String>();
	
	static String generateToken(String user)
	{
		String token = "";
		StringBuilder sb = new StringBuilder(20);
		
		for (int i = 0; i < 20; i++)
		{ 
			int index = (int)(AlphaNumericString.length() * Math.random());
	        sb.append(AlphaNumericString.charAt(index));
	    }
		
		token = sb.toString();
		
		if (tokens.containsKey(token))
			tokens.remove(user);
		
		tokens.put(user, token);
		return token;
	}
	
	static boolean authenticateToken(String user, String token)
	{
		if (tokens.get(user) == null)
			return false;
		else
			return tokens.get(user).equals(token);
	}
	
	static void unauthenticateToken(String user, String token)
	{
		if (tokens.containsValue(token))
			tokens.remove(user, token);
	}
}
