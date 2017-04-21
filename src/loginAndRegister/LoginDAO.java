package loginAndRegister;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import loginAndRegister.LoginBean;

public class LoginDAO {
	
public static void validateLogin(LoginBean LBean, Connection c) {
		
	PreparedStatement s = null;
	ResultSet r = null;
	
		try {
			s = c.prepareStatement("SELECT * FROM users WHERE email = ? AND verified = 1");
			
			String email = LBean.getEmail();
			String pwd = LBean.getPassword();
			
			s.setString(1,email);
			r = s.executeQuery();
			
			if (r.next()) {
				//should be at most, 1 entry
				if (r.getString("password").equals(pwd)) {
					LBean.setUsername(r.getString("username"));
					LBean.setUserID(r.getString("userID"));
					LBean.setLoginOK();
				}
			}
		}
		catch (SQLException e) {
			LBean.triggerSQLError();
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
