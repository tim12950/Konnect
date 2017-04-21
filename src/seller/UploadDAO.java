package seller;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import listings.ListingBean;

public class UploadDAO {
	
	public static void upload(Connection c, ListingBean listing, int hasPicture, String title, String price, String description, String category, String userID, InputStream pic){
		
		if (hasPicture == 0) {
			
			PreparedStatement s = null;
			PreparedStatement t = null;
			ResultSet r = null;
			
			try {                                                               //1      2             3            4           5        6       7     8
				s = c.prepareStatement("INSERT INTO listings(price, description, hasPicture, listingName, category, school, userID, date) VALUES (?,?,?,?,?,?,?,?)");
				t = c.prepareStatement("SELECT email FROM users where userID = ?");
				
				fillMethod(s,t,r,hasPicture,title,price,description,category,userID);
				
				s.setString(8, getDate());
				
				s.executeUpdate();
				listing.setUploadSuccess();
			}
			catch (SQLException e) {
				listing.triggerSQLError();
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
					t.close();
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
		else {
			
			PreparedStatement s = null;
			PreparedStatement t = null;
			ResultSet r = null;
			
			try {                                                               //1      2            3             4           5        6      7      8        9
				s = c.prepareStatement("INSERT INTO listings(price, description, hasPicture, listingName, category, school, userID, picture, date) VALUES (?,?,?,?,?,?,?,?,?)");
				t = c.prepareStatement("SELECT email FROM users where userID = ?");
				
				fillMethod(s,t,r,hasPicture,title,price,description,category,userID);
				
				s.setBlob(8, pic);
				s.setString(9, getDate());
				
				s.executeUpdate();
				listing.setUploadSuccess();
			}
			catch (SQLException e) {
				listing.triggerSQLError();
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
					t.close();
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
	
	public static void fillMethod(PreparedStatement s, PreparedStatement t, ResultSet r, int hasPicture, String title, String price, String description, String category, String userID) throws SQLException{
		
		//null pointer exceptions won't be thrown
		t.setString(1, userID);
		r = t.executeQuery();
		r.next(); 
		
		String email = r.getString("email"); //Any invocation of a ResultSet method which requires a current row will result in a SQLException being thrown. So if userID not in DB, SQLException thrown here
		
		s.setString(1, price);
		s.setString(2, description);
		s.setInt(3, hasPicture);
		s.setString(4, title);
		s.setString(5, category);
		
		if (email.endsWith("@email.kpu.ca")) {
			s.setString(6, "KPU");
		}
		else if (email.endsWith("@ubc.ca")) {
			s.setString(6, "UBC");
		}
		else if (email.endsWith("@sfu.ca")) {
			s.setString(6, "SFU");
		}
		else if (email.endsWith("@bcit.ca")) {
			s.setString(6, "BCIT");
		}
		else {
			s.setString(6, "Unknown");
		}
		
		s.setString(7, userID);
		
	}
	
	public static String getDate() {
		
		DateFormat dateFormat = new SimpleDateFormat("MMMMMMMMM dd, yyyy");
		dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
		Date date = new Date();
		
		return dateFormat.format(date);
		
	}

}
