package common;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieRelated {
	
	public static Cookie getCookie(HttpServletRequest request) {
		
		Cookie[] arrayOfCookies = request.getCookies();
		
		if (arrayOfCookies != null) {
			int length = arrayOfCookies.length;
			
			for(int i = 0; i < length; i++) {
				if (arrayOfCookies[i].getName().equals("konnect")) {
					return arrayOfCookies[i];
				}
			}
		}
		return null;
	}

	public static String getUsername(Cookie k) {
		
		Connection c = DBConnection.getConnection();
		PreparedStatement s = null;
		ResultSet r = null;
		
		try {
			s = c.prepareStatement("SELECT username FROM users WHERE userID = ?");
			
			s.setString(1, k.getValue());
			r = s.executeQuery();
			r.next();
			
			return r.getString("username");
			
		}
		catch (SQLException e) {
			return null;
		}
		finally {
			try {
				r.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				s.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				c.close();
			}
			catch (Exception e) {
				//do nothing
			}
		}
		
	}

}
