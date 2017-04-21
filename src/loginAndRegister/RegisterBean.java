package loginAndRegister;

import common.Bean;

public class RegisterBean extends Bean {
	
	private String email;
	private String password;
	private String username;
	private String confirmPassword;
	private boolean RegOK = false;
	private boolean everythingOK = false;
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setUsername(String uname) {
		username = uname;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setPassword(String pwd) {
		password = pwd;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setConfirmPassword(String pwd) {
		confirmPassword = pwd;
	}
	
	public String getConfirmPassword() {
		return confirmPassword;
	}
	
	public void setRegOK() {
		RegOK = true;
	}
	
	public boolean getRegOK() {
		return RegOK;
	}
	
	public void setEverythingOK() {
		everythingOK = true;
	}
	
	public boolean getEverythingOK() {
		return everythingOK;
	}

}
