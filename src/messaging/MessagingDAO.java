package messaging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import messaging.Chain;
import messaging.Msg;
import messaging.SortChains;

public class MessagingDAO {
	
	//gets list of chains
		public static ArrayList<Chain> getChains(Connection c, String userID, String buyerOrSeller) {
			
			ArrayList<Chain> chains = new ArrayList<Chain>();
			PreparedStatement s = null;
			PreparedStatement t = null;
			PreparedStatement u = null;
			PreparedStatement v = null;
			ResultSet p = null;
			ResultSet q = null;
			ResultSet r = null;
			
			try {
				s = c.prepareStatement("SELECT chainID, listingID, sellerID FROM chains WHERE buyerID = ?");
				t = c.prepareStatement("SELECT chainID, listingID, buyerID FROM chains WHERE sellerID = ?");
				u = c.prepareStatement("SELECT listingName FROM listings WHERE listingID = ?");
				v = c.prepareStatement("SELECT username FROM users WHERE userID = ?");
				
				if (buyerOrSeller.equals("buyer")) {
					s.setString(1, userID);
					p = s.executeQuery();
					if (p.next()) {
						do {
							Chain chain = new Chain();
							
							chain.setChainID(p.getInt("chainID"));
							
							chain.setContainsNewMsg(getChainContainsNewMsg(c, p.getInt("chainID"), userID));
							chain.setLargestMsgID(getmaxMsgID(c,p.getInt("chainID")));
							
							u.setInt(1, p.getInt("listingID")); //listing must exist and be unique
							q = u.executeQuery();
							q.next();
							chain.setListingName(q.getString("listingName"));
							
							v.setString(1, p.getString("sellerID"));
							r = v.executeQuery();
							r.next();
							chain.setSellerName(r.getString("username"));
							chains.add(chain);
						}
						while(p.next());
					}
				}
				else {
					t.setString(1, userID);
					p = t.executeQuery();
					if (p.next()) {
						do {
							Chain chain = new Chain();
							
							chain.setChainID(p.getInt("chainID"));
							
							chain.setContainsNewMsg(getChainContainsNewMsg(c, p.getInt("chainID"), userID));
							chain.setLargestMsgID(getmaxMsgID(c,p.getInt("chainID")));
							
							u.setInt(1, p.getInt("listingID")); //listing must exist and be unique
							q = u.executeQuery();
							q.next();
							chain.setListingName(q.getString("listingName"));
							
							v.setString(1, p.getString("buyerID"));
							r = v.executeQuery();
							r.next();
							chain.setBuyerName(r.getString("username"));
							
							chains.add(chain);
						}
						while(p.next());
					}
				}
				return SortChains.sortChains(c, chains);
			}
			catch (SQLException e) {
				return null;
			}
			finally {
				try {
					p.close();
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
					v.close();
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
		
		//checks to see if chain contains new message
		public static boolean getChainContainsNewMsg(Connection c, int chainID, String userID) throws SQLException{
			
			PreparedStatement s = null;
			ResultSet r = null;
			
			try {
				s = c.prepareStatement("SELECT msgID FROM messages WHERE readStatus = 0 AND chainID = ? AND recipID = ?");
				
				s.setInt(1, chainID);
				s.setString(2, userID);
				
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
			}
		}
		
		public static int getmaxMsgID(Connection c, int chainID) throws SQLException{
			
			PreparedStatement s = c.prepareStatement("SELECT msgID FROM messages WHERE chainID = ? ORDER BY msgID DESC");
			ResultSet r;
			
			s.setInt(1, chainID);
			r = s.executeQuery();
			r.next();
			
			try {
				//must exist
				return r.getInt("msgID");
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
		
		public static ArrayList<Msg> getABuyChain(Connection c, String userID, int chainID) {
			
			ArrayList<Msg> msgs = new ArrayList<Msg>();
			PreparedStatement s = null;
			ResultSet r = null;
			
			try {
				s = c.prepareStatement("SELECT msgID, msg, time, senderID, sellerID FROM messages m, chains c WHERE m.chainID = ? AND m.chainID = c.chainID AND c.buyerID = ? ORDER BY msgID DESC");
				
				s.setInt(1, chainID);
				s.setString(2, userID);
				
				r = s.executeQuery();
				
				if (r.next()) {
					String sellerUsername = getUsername(c,r.getString("sellerID"));
					//String buyerUsername = getUsername(c, userID);
					do {
						Msg msg = new Msg();
						msg.setMsg(r.getString("msg"));
						
						if (r.getString("senderID").equals(userID)) {
							msg.setAnnotation("Sent by you, on " + r.getString("time") + " PST:");
						}
						else {
							msg.setAnnotation("From " + sellerUsername + " on " + r.getString("time") + " PST:");
						}
						msgs.add(msg);
					}
					while(r.next());
				}
				return msgs;
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
		
	public static ArrayList<Msg> getASellChain(Connection c, String userID, int chainID) {
			
			ArrayList<Msg> msgs = new ArrayList<Msg>();
			PreparedStatement s = null;
			ResultSet r = null;
			
			try {
				s = c.prepareStatement("SELECT msgID, msg, time, senderID, buyerID FROM messages m, chains c WHERE m.chainID = ? AND m.chainID = c.chainID AND c.sellerID = ? ORDER BY msgID DESC");
				
				s.setInt(1, chainID);
				s.setString(2, userID);
				
				r = s.executeQuery();
				
				if (r.next()) {
					String buyerUsername = getUsername(c,r.getString("buyerID"));
					do {
						Msg msg = new Msg();
						msg.setMsg(r.getString("msg"));
						
						if (r.getString("senderID").equals(userID)) {
							msg.setAnnotation("Sent by you, on " + r.getString("time") + " PST:");
						}
						else {
							msg.setAnnotation("From " + buyerUsername + " on " + r.getString("time") + " PST:");
						}
						msgs.add(msg);
					}
					while(r.next());
				}
				return msgs;
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
		
	public static String getUsername(Connection c, String userID) throws SQLException {
		PreparedStatement s = c.prepareStatement("SELECT username FROM users WHERE userID = ?");
		ResultSet r;
		s.setString(1, userID);
		r = s.executeQuery();
		r.next();
		
		try {
			//must exist
			return r.getString("username");
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
	
	public static String userChainRelationshipExists(Connection c, String userID, int chainID, String buyerOrSeller) {
		
		PreparedStatement s = null;
		PreparedStatement t = null;
		PreparedStatement u = null;
		ResultSet r = null;
		ResultSet q = null;
		
		try {
			s = c.prepareStatement("SELECT chainID FROM chains WHERE chainID = ?");
			t = c.prepareStatement("SELECT buyerID FROM chains WHERE buyerID = ? AND chainID = ?");
			u = c.prepareStatement("SELECT sellerID FROM chains WHERE sellerID = ? AND chainID = ?");
			
			s.setInt(1, chainID);
			r = s.executeQuery();
			
			if (!(r.next())) {
				return "keepgoing";
			}
			else {
				if (buyerOrSeller.equals("buyer")) {
					t.setString(1, userID);
					t.setInt(2, chainID);
					q = t.executeQuery();
					
					if (!(q.next())) {
						return "no";
					}
					else {
						return "keepgoing";
					}
				}
				else {
					u.setString(1, userID);
					u.setInt(2, chainID);
					q = u.executeQuery();
					
					if (!(q.next())) {
						return "no";
					}
					else {
						return "keepgoing";
					}
				}
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
