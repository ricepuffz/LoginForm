package me.nico.loginform;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

@WebServlet("/StockServlet")
public class StockServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;
	//Stealing the api key is of no use since it's just a free plan, get your own at alphavantage.co
	private static final String API_KEY = "TBMBG4EOCQK7Y6EM";
       
    public StockServlet()
    {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		PrintWriter out = response.getWriter();
		String logoutTag = request.getParameter("logout");
		
		if (logoutTag != null && logoutTag.equals("true"))
		{
			SessionManager.unauthenticateToken(request.getParameter("username"), request.getParameter("token"));
			response.sendRedirect(request.getContextPath() + "/LoginServlet");
			return;
		}
		
		if (request.getParameter("symbol") == null)
			response.sendRedirect(request.getContextPath() + "/ErrorServlet?username=" + request.getParameter("username") + "&token=" + request.getParameter("token") + "&message=No symbol specified");
		else
		{
			String token = request.getParameter("token");
			String username = request.getParameter("username");
			
			if (token == null || username == null || !SessionManager.authenticateToken(username, token))
				response.sendRedirect("LoginServlet");
			else
			{
				String symbol = request.getParameter("symbol").toUpperCase();
				String type = request.getParameter("type");
				
				switch (type) {
					case "volume":
						break;
					case "high":
						break;
					case "low":
						break;
					case "open":
						break;
					case "close":
						break;
					default:
						out.print("<html><body><p>ERROR: INVALID TYPE</p><a href=\"MainPageServlet?username=" + request.getParameter("username") + "&token=" + request.getParameter("token") + "\">Return to Main Page</a></body></html>");
				}				
				
				//0:1min, 1:5min, 2:15min, 3:30min
				JSONObject[] dataJSON = new JSONObject[4];
				
				for (int i = 0; i < dataJSON.length; i++)
				{
					String intervall = "";
					
					switch (i) {
						case 0:
							intervall = "1min";
							break;
						case 1:
							intervall = "5min";
							break;
						case 2:
							intervall = "15min";
							break;
						case 3:
							intervall = "30min";
							break;
					}
					String url = "https://www.alphavantage.co/query?function=TIME_SERIES_INTRADAY&symbol=" + symbol + "&interval=" + intervall + "&apikey=" + API_KEY;
					dataJSON[i] = new JSONObject(Requester.sendGET(url));
				}
				
				for (JSONObject data : dataJSON)
				{
					String firstKey = data.keys().next();
					if (firstKey.equals("Error Message"))
					{
						response.sendRedirect(request.getContextPath() + "/ErrorServlet?username=" + username + "&token=" + token + "&message=Invalid symbol");
						return;
					} else if (firstKey.equals("Note")) {
						response.sendRedirect(request.getContextPath() + "/ErrorServlet?username=" + username + "&token=" + token + "&message=API call frequency limit reached (5per minute, 500 per day)");
						return;
					}
				}
				
				String[] html = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/stockPage.html");
				String[] navigationBarHtml = HTMLReader.readHtml(GlobalInfo.PROJECT_PATH + "/WebContent/navigationBar.html");
				String stockInfo = WikipediaSummaryFetcher.fetch(SymbolConversion.symbolToCompanyName(symbol));
				
				if (stockInfo.equals("")) {
					stockInfo = "No information is available for this stock.";
				}
				
				out.print(html[0] + request.getContextPath() + html[1] + symbol + html[2] + navigationBarHtml[0] + request.getContextPath() + navigationBarHtml[1] + html[3]
						+ stockInfo + html[4] + type.substring(0, 1).toUpperCase() + type.substring(1) + html[5] + symbol + html[6]
						+ buildGraph("min1Chart", symbol, type, "1min", dataJSON[0])
						+ buildGraph("min5Chart", symbol, type, "5min", dataJSON[1])
						+ buildGraph("min15Chart", symbol, type, "15min", dataJSON[2])
						+ buildGraph("min30Chart", symbol, type, "30min", dataJSON[3])
						+ html[7]);
			}
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String searchInput = request.getParameter("navSearchInput");
		if (searchInput == null)
			doGet(request, response);
		else
		{
			String type = request.getParameter("type");
			String[] searchParams = searchInput.split(":");
			if (searchParams.length > 1)
				type = searchParams[1];
			response.sendRedirect(request.getContextPath() + "/StockServlet?symbol=" + searchParams[0] + "&type=" + type + "&username=" + request.getParameter("username") + "&token=" + request.getParameter("token"));
		}
	}
	
	
	private String buildGraph(String id, String symbol, String type, String intervall, JSONObject dataJSON)
	{
		String[] data = getData(symbol, type, intervall, dataJSON);
		boolean beginAtZero = type.equals("volume");
		
		return "var ctx = document.getElementById('" + id + "');"
				+ "var " + id + " = new Chart(ctx, {"
				+ "    type: 'line',"
				+ "    data: {"
				+ "        labels: [" + (String) intervallString(intervall)[0] + "],"
				+ "        datasets: [{"
				+ "            label: '"+ type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase() + "',"
				+ "            data: [" + data[0] + ", " + data[1] + ", " + data[2] + ", " + data[3] + ", " + data[4] + ", " + data[5] + "],"
				+ "            backgroundColor: 'rgba(255, 99, 132, 0.2)',"
				+ "            borderColor: 'rgba(255, 99, 132, 1)',"
				+ "            borderWidth: 2"
				+ "        }]"
				+ "    },"
				+ "    options: {"
				+ "        scales: {"
				+ "            yAxes: [{"
				+ "                ticks: {"
				+ "                    beginAtZero: " + beginAtZero
				+ "                }"
				+ "            }]"
				+ "        },"
				+ "        elements: {"
				+ "            line: {"
				+ "                "//tension: 0"
				+ "            }"
				+ "        }"
				+ "    }"
				+ "});";
	}
	
	@SuppressWarnings("deprecation")
	private String getIntervallString(String intervall)
	{
		String intervallString = "'";
		int intervallInt = Integer.parseInt(intervall.split("m")[0]);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.HOUR, -5);
		if (intervallInt == 1)
			calendar.add(Calendar.MINUTE, -1);
		calendar.set(Calendar.MINUTE, calendar.get(Calendar.MINUTE) / intervallInt * intervallInt);
		
		for (int i = 0; i < 6; i++)
		{
			Date time = calendar.getTime();
			//timeStrings[0]: hours, timeStrings[1]: minutes
			String[] timeStrings = new String[] { String.valueOf(time.getHours()), String.valueOf(time.getMinutes()) };
			
			for (int j = 0; j < timeStrings.length; j++)
				timeStrings[j] = String.valueOf(Integer.parseInt(timeStrings[j]) < 10 ? "0" + timeStrings[j] : timeStrings[j]);
			
			intervallString += timeStrings[0] + ":" + timeStrings[1] + "', '";
			calendar.add(Calendar.MINUTE, -intervallInt);
		}
		String newIntervallString = inverse(intervallString);
		return newIntervallString.substring(0, newIntervallString.length() - 3); //intervallString.substring(0, intervallString.length() - 3);
	}
	
	private String inverse(String intervallString)
	{
		String[] parts = intervallString.split("', '");
		parts[0] = parts[0].substring(1);
		String newIntervallString = "'";
		
		for (int i = 5; i >= 0; i--)
			newIntervallString = newIntervallString + parts[i] + "', '";
		
		return newIntervallString;
	}

	private String getStandardIntervallString(String intervall)
	{
		switch (intervall) {
			case "1min":
				return "'15:55', '15:56', '15:57', '15:58', '15:59', '16:00'";
			case "5min":
				return "'15:35', '15:40', '15:45', '15:50', '15:55', '16:00'";
			case "15min":
				return "'14:45', '15:00', '15:15', '15:30', '15:45', '16:00'";
			case "30min":
				return "'13:30', '14:00', '14:30', '15:00', '15:30', '16:00'";
			default:
				return "";
		}
	}
	
	//Returns [0]: intervallString, [1] dataToUse calendar
	@SuppressWarnings("deprecation")
	private Object[] intervallString(String intervall)
	{
		String intervallString = "";
		
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.HOUR, -5);
	    Date now = calendar.getTime();
	    Calendar openingClosingCalendar = Calendar.getInstance();
	    openingClosingCalendar.set(Calendar.HOUR, 9);
	    openingClosingCalendar.set(Calendar.MINUTE, 30);
	    openingClosingCalendar.set(Calendar.AM_PM, Calendar.AM);
	    Date openingTime = openingClosingCalendar.getTime();
	    openingClosingCalendar.set(Calendar.HOUR, 4);
	    openingClosingCalendar.set(Calendar.MINUTE, 0);
	    openingClosingCalendar.set(Calendar.AM_PM, Calendar.PM);
	    Date closingTime = openingClosingCalendar.getTime();
	    
	    if (now.getHours() < openingTime.getHours() || now.getHours() == openingTime.getHours() && now.getMinutes() < openingTime.getMinutes()) {
	    	intervallString = getStandardIntervallString(intervall);
	    	if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY)
	    		calendar.add(Calendar.DATE, -3);
	    	else
	    		calendar.add(Calendar.DATE, -1);
	    } else if (now.getHours() > closingTime.getHours() || now.getHours() == closingTime.getHours() && now.getMinutes() > closingTime.getMinutes())
	    	intervallString = getStandardIntervallString(intervall);
	    else
	    	intervallString = getIntervallString(intervall);
	    
	    return new Object[] { intervallString, calendar };
	}
	
	private String[] getData(String symbol, String type, String intervall, JSONObject dataJSON)
	{
		System.out.println(dataJSON.toString());

		dataJSON = dataJSON.getJSONObject("Time Series (" + intervall + ")");
		
		Object[] intervallStringResult = intervallString(intervall);
		
		String intervallString = (String) intervallStringResult[0];
	    
	    Date dateToUse = ((Calendar) intervallStringResult[1]).getTime();
		
		String date = new SimpleDateFormat("yyyy-MM-dd").format(dateToUse);
		String[] times = intervallString.split("', '");
		times[0] = times[0].substring(1);
		times[5] = times[5].substring(0, 5);
		
		for (int i = 0; i < times.length; i++)
			times[i] += ":00";
		
		String[] data = new String[6];
		
		String typeString = "";
		switch (type) {
		case "volume":
			typeString = "5. volume";
			break;
		case "high":
			typeString = "2. high";
			break;
		case "low":
			typeString = "3. low";
			break;
		case "open":
			typeString = "1. open";
			break;
		case "close":
			typeString = "4. close";
			break;
		}
		
		for (int i = 0; i < data.length; i++)
		{
			try {
				data[i] = dataJSON.getJSONObject(date + " " + times[i]).getString(typeString);
			} catch (JSONException e) {
				data[i] = "null"; //0
			}
		}
		
		return data;
	}

	/*
	private String reduceTime(String time, int minutes)
	{
		//0: hours, 1: minutes, 2: seconds
		String[] fragments = time.split(":");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR, Integer.parseInt(fragments[0]));
		calendar.set(Calendar.MINUTE, Integer.parseInt(fragments[1]));
		calendar.add(Calendar.MINUTE, minutes);
		
		int hourInt = calendar.get(Calendar.HOUR);
		int minuteInt = calendar.get(Calendar.MINUTE);
		
		String hour = hourInt < 10 ? "0" + String.valueOf(hourInt) : String.valueOf(hourInt);
		String minute = minuteInt < 10 ? "0" + String.valueOf(minuteInt) : String.valueOf(minuteInt);
		if (hour.equals("00"))
			hour = "12";
		
		return hour + ":" + minute + ":00";
	}
	*/
}
