package seller;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

import listings.ListingBean;

public class EditDAO {
	
	public static void edit(int listingID, Connection c, ListingBean listing, String title, int hasPicture, String price, String money, String description, String category, InputStream pic ) {
		
		PreparedStatement s = null;
		
		try {	//no picture
			if (hasPicture == 0) {
				s = c.prepareStatement("UPDATE listings SET listingName = ?, description = ?, price = ?, category = ? WHERE listingID = ?");
				
				fill(s,title,price,money,category,description);
				s.setInt(5, listingID);
				s.executeUpdate();
				
				listing.setUploadSuccess();
			}
			else { //picture:
				s = c.prepareStatement("UPDATE listings SET listingName = ?, description = ?, price = ?, category = ?, hasPicture = ?, picture = ? WHERE listingID = ?");
				
				s.setInt(5, 1);
				fill(s,title,price,money,category,description);
				s.setBlob(6, pic);
				s.setInt(7, listingID);
				s.executeUpdate();
				
				listing.setUploadSuccess();
			}
		}
		catch (SQLException e) {
			listing.triggerSQLError();
		}
		finally {
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
	
	public static void fill(PreparedStatement s, String title, String price, String money, String category, String description) throws SQLException {
		
		//don't think null pointer exception would ever be thrown here, because sqlexception would be thrown in edit method above first
		s.setString(1, title);
		s.setString(2, description);
		
		if (price.equals("specify")) {
			s.setString(3, money);
		}
		else {
			s.setString(3, price);
		}
		
		s.setString(4, category);
	}
	
	public static void editDeletePic(int listingID, Connection c, ListingBean listing, String title, String price, String money, String description, String category) {
		
		PreparedStatement s = null;
		
		try {
			s = c.prepareStatement("UPDATE listings SET listingName = ?, description = ?, price = ?, category = ?, hasPicture = ?, picture = ? WHERE listingID = ?");
			
			s.setInt(5, 0);
			fill(s,title,price,money,category,description);
			s.setNull(6, Types.BLOB);
			s.setInt(7, listingID);
			s.executeUpdate();
		}
		catch (SQLException e) {
			listing.triggerSQLError();
		}
		finally {
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
