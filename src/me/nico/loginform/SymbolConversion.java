package me.nico.loginform;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class SymbolConversion
{
	public static String symbolToCompanyName(String symbol)
	{
		String link = "https://query1.finance.yahoo.com/v1/finance/search?q=" + symbol + "&quotesCount=1&newsCount=0&quotesQueryId=tss_match_phrase_query&multiQuoteQueryId=multi_quote_single_token_query&newsQueryId=news_ss_symbols&enableCb=false&enableNavLinks=false";
		JSONObject json = null;
		try {
			json = new JSONObject(Requester.sendGET(link));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (json == null) { return ""; }
		
		int count = (Integer) json.get("count");
		if (count < 1) { return ""; }
		
		JSONArray quotes = json.getJSONArray("quotes");
		if (quotes.isEmpty()) { return ""; }
		
		JSONObject firstQuote = quotes.getJSONObject(0);
		if (!(firstQuote.has("symbol") && firstQuote.has("longname"))) { return ""; }
		if (!((String) firstQuote.get("symbol")).equalsIgnoreCase(symbol)) { return ""; }
		
		return (String) firstQuote.get("shortname");
	}
}
