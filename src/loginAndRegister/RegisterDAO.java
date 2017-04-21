package loginAndRegister;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import loginAndRegister.RegisterBean;

public class RegisterDAO {
	
	public static void insertIntoUsers(RegisterBean RBean, Connection k, String id, String code) throws SQLException{
		
		PreparedStatement s = null;
		String email = RBean.getEmail();
		String pwd = RBean.getPassword();
		String username = RBean.getUsername();
		
		try {
			s = k.prepareStatement("INSERT INTO users(userID, email, password, username, code, verified) VALUES (?,?,?,?,?,0)");
			
			s.setString(1, id);
			s.setString(2, email);
			s.setString(3, pwd);
			s.setString(4, username);
			s.setString(5, code);
			s.executeUpdate();
		}
		catch (SQLException e) {
			throw new SQLException();
		}
		finally {
			try {
				s.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				k.close();
			}
			catch (Exception e) {
				//do nothing
			}
		}
	}
	
	public static void validateReg(RegisterBean RBean, Connection c, Connection d) {
		
		String email = RBean.getEmail();
		String pwd = RBean.getPassword();
		String confPwd = RBean.getConfirmPassword();
		String username = RBean.getUsername();
		StringBuilder msg = new StringBuilder("");
		
		if (emailOK(email,c,msg,RBean) && passwordOK(pwd,confPwd,msg) && usernameOK(username,d,msg,RBean)) {
			RBean.setRegOK();
		}
		else {
			RBean.setErrorMsg(msg.toString());
		}
	}
	
	public static boolean emailOK(String email, Connection c, StringBuilder msg, RegisterBean RBean) {
		
		int emailLength = email.length(); //if user does not enter anything length = 0.
		int index = 0;
		int count = 0;
		PreparedStatement s = null;
		ResultSet r = null;
		
		try {
			s = c.prepareStatement("SELECT email FROM users WHERE verified = 1");
			r = s.executeQuery();
				
			while (r.next()) {
				if (r.getString("email").equals(email)) {
					msg.append("there's already an account with this email");
					return false;
				}
			}
		}
		catch (SQLException e) {
			RBean.triggerSQLError();
			return false;
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
		
		if (emailLength > 64) {
			msg.append("email can't be longer than 64 characters");
			return false;
		}
		
		for(int i = emailLength-1; i >= 0; i--) {
			if (email.charAt(i) == '@') {
				index = i;
				break;
			}
			else {
				count = count + 1;
			}
		}
		
		if (count == emailLength) {
			msg.append("invalid email");
			return false;
		}
		else {
			String domain = email.substring(index);
			int domainLength = domain.length();
			if (domainLength >= 2 && emailLength > domainLength) {
				return true;
			}
			else {
				msg.append("invalid email");
				return false;
			}
		}
	}
	
	public static boolean passwordOK(String password, String pwdA, StringBuilder msg) {
		if (!(password.equals(""))) {
			if (!(password.charAt(0) == ' ' || password.charAt(password.length() - 1) == ' ')) {
				if (password.length() <= 32) {
					if (password.equals(pwdA)) {
						return true;
					}
					else {
						msg.append("password mismatch");
						return false;
					}
				}
				else {
					msg.append("password can't be longer than 32 characters");
					return false;
				}
			}
			else {
				msg.append("password can't begin or end with spaces");
				return false;
			}
		}
		else {
			msg.append("password field can't be blank");
			return false;
		}		
	}
	
	public static boolean usernameOK(String username, Connection d, StringBuilder msg, RegisterBean RBean) {
		int length = username.length();
		PreparedStatement s = null;
		ResultSet r = null;
		
		if (length == 0) {
			msg.append("username field can't be blank");
			return false;
		}
		
		try {
			s = d.prepareStatement("SELECT username FROM users");
			r = s.executeQuery();
			
			while (r.next()) {
				if (r.getString("username").equals(username)) {
					msg.append("there's already an account with this username");
					return false;
				}
			}
		}
		catch (SQLException e) {
			RBean.triggerSQLError();
			return false;
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
				d.close();
			}
			catch (Exception e) {
				//do nothing
			}
		}
		
		if (length > 32) {
			msg.append("username can't be longer than 32 characters");
			return false;
		}
		
		for(int i = 0; i < length; i++) {
			if (username.contains(" ")) {
				msg.append("username can't contain spaces");
				return false;
			}
		}
		
		return true;
	}

}
