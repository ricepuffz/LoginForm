package me.nico.loginform;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Requester
{
	public static String sendGET(String url) throws IOException
	{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		if (con.getResponseCode() == HttpURLConnection.HTTP_OK)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			return response.toString();
		} else {
			System.out.println("GET request failed!");
			return "";
		}
	}
}
