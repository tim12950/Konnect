package common;

public class Bean {
	
	private boolean SQLErrorState = false;
	private boolean IOErrorState = false;
	private String ErrorMsg;
	
	public void triggerSQLError() {
		SQLErrorState = true;
	}
	
	public boolean getSQLErrorState() {
		return SQLErrorState;
	}
	
	public void triggerIOError() {
		IOErrorState = true;
	}
	
	public boolean getIOErrorState() {
		return IOErrorState;
	}
	
	public void setErrorMsg(String s) {
		ErrorMsg = s;
	}
	
	public String getErrorMsg() {
		return ErrorMsg;
	}

}
