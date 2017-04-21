package listings;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import common.Bean;

public class ListingBean extends Bean{
	
	private int listingID;
	private String listingName;
	private String description;
	private String price;
	private String category;
	private String date;
	private int hasPicture = 0;
	private String picture;
	private String school;
	private String sellerName;
	private boolean uploadSuccessful = false;
	private boolean listingExists = false;
	
	public int getlistingID() {
		return listingID;
	}
	
	public void setlistingID(int n) {
		listingID = n;
	}
	
	public String getName() {
		return listingName;
	}
	
	public void setName(String s) {
		listingName = s;
	}
	
	public String getPrice() {
		return price;
	}
	
	public void setPrice(String s) {
		price = s;
	}
	
	public void setCategory(String s) {
		category = s;
	}
	
	public String getCategory() {
		return category;
	}
	
	public void setSchool(String s) {
		school = s;
	}
	
	public String getSchool() {
		return school;
	}
	
	public void setSeller(String s) {
		sellerName = s;
	}
	
	public String getSeller() {
		return sellerName;
	}
	
	public void setDescription(String s) {
		description = s;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setPicture(InputStream pic) throws IOException{
		
		byte[] bytes = new byte[102400];
		bytes = IOUtils.toByteArray(pic); //does it throw exception if image too large?
		String base64String = Base64.encodeBase64String(bytes);
		picture = base64String;
		
	}
	
	public String getPicture() {
		return picture;
	}
	
	public void setDate(String s) {
		date = s;
	}
	
	public String getDate() {
		return date;
	}
	
	public int getHasPicture() {
		return hasPicture;
	}
	
	public void setHasPicture() {
		hasPicture = 1;
	}
	
	public void setUploadSuccess() {
		uploadSuccessful = true;
	}
	
	public boolean getUploadSuccess() {
		return uploadSuccessful;
	}
	
	public void setListingExists() {
		listingExists = true;
	}
	
	public boolean getListingExists() {
		return listingExists;
	}

}
