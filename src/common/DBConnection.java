package common;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	
public static Connection getConnection() {
		
		Connection c = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			//if you don't have this it won't work; find out why
			c = DriverManager.getConnection("jdbc:mysql://mydbinstance.cors8hxf3tff.us-west-2.rds.amazonaws.com:3306/mydb", "*", "*");
			//this connection object is a connection to our local database which requires a username and password.
		}
		catch (ClassNotFoundException e) {
			//will this error actually ever be thrown? if so, returns null
		}
		catch (SQLException e) {
			//return null
		}
		
		return c;
	}

}
