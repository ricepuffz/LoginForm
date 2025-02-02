package me.nico.loginform;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class UserDatabase
{
	Statement statement = null;
	
	public UserDatabase() throws ClassNotFoundException, SQLException
	{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=Europe/London", "root", "toor");
		
		statement = connection.createStatement();
	}
	
	public boolean verify(String username, String password)
	{
		ResultSet result = null;
		try {
			result = statement.executeQuery("SELECT * FROM data WHERE email = \"" + username + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (result.next())
				return result.getString(2).equals(password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public String getName(String username)
	{
		ResultSet result = null;
		try {
			result = statement.executeQuery("SELECT * FROM data WHERE email = \"" + username + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (result.next())
				return result.getString(3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getFirstName(String username)
	{
		ResultSet result = null;
		try {
			result = statement.executeQuery("SELECT * FROM data WHERE email = \"" + username + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (result.next())
				return result.getString(4);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getGender(String username)
	{
		ResultSet result = null;
		try {
			result = statement.executeQuery("SELECT * FROM data WHERE email = \"" + username + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			if (result.next())
				return result.getString(5);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public void register(String username, String password, String gender, String name, String firstName)
	{
		try {
			statement.execute("INSERT INTO data VALUE (\"" + username + "\", \"" + password + "\", \"" + name + "\", \"" + firstName + "\", \"" + gender + "\"");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
