package listings;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import common.Bean;
import listings.ListingBean;

public class ListingDAO {
	
	//gets all listings
		public static ArrayList<ListingBean> getListings(Bean bean, Connection c) {
			
			ArrayList<ListingBean> listings = new ArrayList<ListingBean>();
			PreparedStatement s = null;
			ResultSet u = null;
			
			try {
				s = c.prepareStatement("SELECT listingID, listingName FROM listings ORDER BY listingID DESC");
				u = s.executeQuery();
				
				if (u.next()) {
					do {
						ListingBean listing = new ListingBean();
						listing.setlistingID(u.getInt("listingID"));
						listing.setName(u.getString("listingName"));
						listings.add(listing);
					}
					while (u.next());
				}
				else {
					return null;
				}
				return listings;
			}
			catch (SQLException e) {
				bean.triggerSQLError();
				return null;
			}
			finally {
				try {
					u.close();
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
		
		//gets listings in specific category
		public static ArrayList<ListingBean> getListingsInCategory(Bean b, Connection c, String category) {
			
			ArrayList<ListingBean> listings = new ArrayList<ListingBean>();
			PreparedStatement s = null;
			ResultSet u = null;
			
			try {
				s = c.prepareStatement("SELECT listingID, listingName FROM listings WHERE category = ? ORDER BY listingID DESC");
				s.setString(1, category);
				u = s.executeQuery();
				
				if (u.next()) {
					do {
						ListingBean listing = new ListingBean();
						listing.setlistingID(u.getInt("listingID"));
						listing.setName(u.getString("listingName"));
						listings.add(listing);
					}
					while (u.next());
				}
				else {
					return null;
				}
				return listings;
			}
			catch (SQLException e) {
				b.triggerSQLError();
				return null;
			}
			finally {
				try {
					u.close();
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
		
		//gets a specific listing
		public static ListingBean getListing(ListingBean listing, Connection c) {
			
			int listingID = listing.getlistingID();
			PreparedStatement s = null;
			PreparedStatement t = null;
			ResultSet u = null;
			ResultSet v = null;
			
			try {
				s = c.prepareStatement("SELECT listingName, price, category, hasPicture, picture, description, school, userID, date FROM listings WHERE listingID = ?");
				t = c.prepareStatement("SELECT username FROM users WHERE userID = ?");
				
				
				s.setInt(1, listingID);
				u = s.executeQuery();
				
				if (u.next()) {
					//listing.setlistingID(listingID); already set listings servlet
					listing.setName(u.getString("listingName"));
					
					if (u.getString("price").equals("free")) {
						listing.setPrice("Free");
					}
					else if (u.getString("price").equals("uponRequest")) {
						listing.setPrice("Available Upon Request");
					}
					else {
						listing.setPrice("$" + u.getString("price"));
					}
					
					listing.setCategory(u.getString("category"));
					listing.setSchool(u.getString("school"));
					listing.setDescription(u.getString("description"));
					listing.setDate(u.getString("date"));
					
					if (u.getInt("hasPicture") == 1) {
						listing.setHasPicture();
						listing.setPicture(u.getBinaryStream("picture")); //throws IO exception
					}
					
					t.setString(1, u.getString("userID"));
					v = t.executeQuery();
					v.next();
					listing.setSeller(v.getString("username"));
					
					listing.setListingExists();
					return listing;
				}
				else {
					//listingexists = false
					return listing;
				}
			}
			catch (SQLException e) {
				listing.triggerSQLError();
				return listing;
			}
			catch (IOException e) {
				listing.triggerIOError();
				return listing;
			}
			finally {
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
