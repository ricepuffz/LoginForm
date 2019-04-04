package me.nico.loginform;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

public class WikipediaSummaryFetcher
{
	static String fetch(String query)
	{
		if (query.isEmpty()) { return ""; }
		query = query.replace(".com", "");
		
		String link = "https://en.wikipedia.org/w/api.php?format=json&action=query&prop=extracts&exintro&explaintext&redirects=1&titles=" + URLEncoder.encode(query);
		System.out.println(link);
		JSONObject data = null;
		
		try {
			String response = Requester.sendGET(link);
			if (response.equals("")) { return ""; }
			data = new JSONObject(response);
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		JSONObject pages = data.getJSONObject("query").getJSONObject("pages");
		int pageID = Integer.parseInt(pages.keys().next());
		if (pageID < 0) { return ""; }
		
		JSONObject page = pages.getJSONObject(String.valueOf(pageID));
		return (String) page.get("extract");
	}
}
