package messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import common.DBConnection;
import messaging.Msg;

public class ComposeMsgsDAO {
	
	public static void sendBrandNewMsgtoDB(Connection c, Msg msg) {
		
		String buyerID = msg.getBuyerID();
		String message = msg.getMsg();
		int listingID = msg.getListingID();
		PreparedStatement s = null;
		PreparedStatement t = null;
		PreparedStatement u = null;
		ResultSet r = null;
		
		try {
			s = c.prepareStatement("INSERT INTO messages(msg, senderID, recipID, readStatus, time, chainID) VALUES (?,?,?,0,?,?)");
			t = c.prepareStatement("INSERT INTO chains(listingID, buyerID, sellerID) VALUES (?,?,?)");
			u = c.prepareStatement("SELECT chainID FROM chains WHERE listingID = ? AND buyerID = ?");
			
			String sellerID = lookupListingSeller(c, listingID);
			int chainID = 0;
			
			if (sellerID != null) {
				if (!(sellerID.equals(buyerID))) {
					if (chainExists(c, listingID, buyerID) == 0) {
						t.setInt(1, listingID);
						t.setString(2, buyerID);
						t.setString(3, sellerID);
						t.executeUpdate();
						
						u.setInt(1, listingID);
						u.setString(2, buyerID);
						
						r = u.executeQuery();
						r.next();
						chainID = r.getInt("chainID");
						
						s.setString(1, message);
						s.setString(2, buyerID);
						s.setString(3, sellerID);
						s.setString(4, getTime());
						s.setInt(5, chainID);
						s.executeUpdate();
						
						msg.setChainID(chainID);
						msg.setUploadSuccess();
					}
					else {
						msg.setErrorMsg("chainexists");
						msg.setChainID(chainExists(c, listingID, buyerID));
					}
				}
				else {
					msg.setErrorMsg("cantbuyfromhimself");
					//tell user he can't buy from himself
				}
			}
			else {
				msg.setErrorMsg("nosuchlisting");
				//listing doesn't exist
			}
		}
		catch (SQLException e) {
			msg.triggerSQLError();
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
				u.close();
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
	
	public static boolean checkiflistingexists(int listingID) throws SQLException{
		
		Connection c = DBConnection.getConnection();
		PreparedStatement s = null;
		ResultSet r = null;
		
		if (c != null) {
			
			try {
				s = c.prepareStatement("SELECT listingID FROM listings WHERE listingID = ?");
				s.setInt(1, listingID);
				r = s.executeQuery();
				
				if (r.next()) {
					return true;
				}
				else {
					return false;
				}
			}
			catch (SQLException e) {
				throw new SQLException();
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
		else {
			throw new SQLException();
		}
	}
	
	public static String lookupListingSeller(Connection c, int listingID) throws SQLException{
			
		PreparedStatement s = c.prepareStatement("SELECT userID FROM listings WHERE listingID = ?");
		ResultSet r;
		
		s.setInt(1, listingID);
		r = s.executeQuery();
		
		try {
			if (r.next()) {
				return r.getString("userID");
			}
			else {
				return null;
			}
		}
		catch (SQLException e) {
			throw new SQLException();
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
		}
	}
	
	public static int chainExists(Connection c, int listingID, String buyerID)  throws SQLException{
		
		PreparedStatement s = c.prepareStatement("SELECT chainID FROM chains WHERE listingID = ? AND buyerID = ?");
		ResultSet r;
		
		s.setInt(1, listingID);
		s.setString(2, buyerID);
		
		r = s.executeQuery();
		
		try {
			if (r.next()) {
				return r.getInt("chainID");
			}
			else {
				return 0;
			}
		}
		catch (SQLException e) {
			throw new SQLException();
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
		}
	}
	
	public static String getTime() {
		
		DateFormat dateFormat = new SimpleDateFormat("MMMMMMMMM dd, yyyy ; hh:mm aa");
		dateFormat.setTimeZone(TimeZone.getTimeZone("PST"));
		Date date = new Date();
		
		return dateFormat.format(date);
		
	}
	
	public static void validateReply(Connection c, Msg msg, String userID) {
		
		int chainID = msg.getChainID();
		PreparedStatement s = null;
		PreparedStatement t = null;
		PreparedStatement u = null;
		ResultSet r = null;
		ResultSet p = null;
		
		try {
			s = c.prepareStatement("SELECT chainID FROM chains WHERE chainID = ?");
			t = c.prepareStatement("SELECT buyerID, sellerID FROM chains WHERE chainID = ?");
			u = c.prepareStatement("INSERT INTO messages(msg, senderID, recipID, readStatus, time, chainID) VALUES (?,?,?,0,?,?)");
			
			
			s.setInt(1, chainID);
			r = s.executeQuery();
			
			if (r.next()) {
				t.setInt(1, chainID);
				p = t.executeQuery();
				p.next();
				
				if (p.getString("buyerID").equals(userID) || p.getString("sellerID").equals(userID)) {
					String recipID = "";
					if (p.getString("buyerID").equals(userID)) {
						msg.setIsSenderBuyerOrSeller("buy");
						recipID = p.getString("sellerID");
						
						if (msg.getMsg().length() == 0) {
							msg.setErrorMsg("empty");
							return;
						}
						else if (msg.getMsg().length() >= 65500) {
							msg.setErrorMsg("toolong");
							return;
						}
					}
					else {
						msg.setIsSenderBuyerOrSeller("sell");
						recipID = p.getString("buyerID");
						
						if (msg.getMsg().length() == 0) {
							msg.setErrorMsg("empty message");
							return;
						}
						else if (msg.getMsg().length() >= 65500) {
							msg.setErrorMsg("too long");
							return;
						}
					}
					
					u.setString(1, msg.getMsg());
					u.setString(2, userID);
					u.setString(3, recipID);
					u.setString(4, getTime());
					u.setInt(5, chainID);
					
					u.executeUpdate();
					
					msg.setUploadSuccess();
				}
				else {
					msg.setErrorMsg("notauth");
				}
			}
			else {
				msg.setErrorMsg("chaindne");
			}
		}
		catch (SQLException e) {
			msg.triggerSQLError();
		}
		finally {
			try {
				r.close();
			}
			catch (Exception e) {
				//do nothing
			}
			try {
				p.close();
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
				u.close();
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
