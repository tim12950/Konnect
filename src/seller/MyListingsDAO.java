package seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.Bean;
import common.DBConnection;
import listings.ListingBean;

public class MyListingsDAO {
	
public static ArrayList<ListingBean> getMyListOfListings(String userID, Bean b, Connection c) {
		
		ArrayList<ListingBean> myListOfListings = new ArrayList<ListingBean>();
		PreparedStatement s = null;
		ResultSet r = null;
		
		try {
			s = c.prepareStatement("SELECT listingID, listingName FROM listings WHERE userID = ?");
			
			s.setString(1, userID);
			r = s.executeQuery();
			
			if (r.next()) {
				do {
					ListingBean listing = new ListingBean();
					listing.setlistingID(r.getInt("listingID"));
					listing.setName(r.getString("listingName"));
					myListOfListings.add(listing);
				}
				while(r.next());
			}
			else {
				return null;
			}
			return myListOfListings;
			
		}
		catch (SQLException e) {
			b.triggerSQLError();
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
	
	public static String verifyUserListingRelationship(int listingID, String userID) { //if sql error thrown here, return "sqlerror", if verified, return "good,", if not verified, return "notgood"
		
		Connection c = DBConnection.getConnection();
		
		if (c != null) {
			
			PreparedStatement s = null;
			PreparedStatement t = null;
			ResultSet r = null;
			ResultSet q = null;
			
			try {
				s = c.prepareStatement("SELECT listingID FROM listings WHERE userID = ? AND listingID = ?");
				t = c.prepareStatement("SELECT listingID FROM listings WHERE listingID = ?");
				
				t.setInt(1, listingID);
				
				r = t.executeQuery();
				
				if (r.next()) {
					//keep going
				}
				else {
					return "notfound";
				}
				
				s.setString(1, userID);
				s.setInt(2, listingID);
				
				q = s.executeQuery();
				
				if (q.next()) {
					return "good";
				}
				else {
					return "notgood";
				}
			}
			catch (SQLException e) {
				return "sqlerror";
			}
			finally {
				try {
					r.close();
				}
				catch (Exception e) {
					//do nothing
				}
				try {
					q.close();
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
			return "sqlerror";
		}
	}

}
