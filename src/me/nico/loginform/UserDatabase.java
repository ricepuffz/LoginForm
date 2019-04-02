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
		Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/users", "root", "toor");
		
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
	
	public static void main(String[] args)
	{
		try {
			new UserDatabase();
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
}
