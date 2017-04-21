package loginAndRegister;

import common.Bean;

public class LoginBean extends Bean {
	
	private String email;
	private String password;
	private String username;
	private String userID;
	private boolean LoginOK = false;
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setPassword(String pwd) {
		password = pwd;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setLoginOK() {
		LoginOK = true;
	}
	
	public boolean getLoginOK() {
		return LoginOK;
	}
	
	public void setUsername(String s) {
		username = s;
	};
	
	public String getUsername() {
		return username;
	}
	
	public void setUserID(String s) {
		userID = s;
	}
	
	public String getUserID() {
		return userID;
	}

}
