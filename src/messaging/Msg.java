package messaging;

import common.Bean;

public class Msg extends Bean{
	
	private int chainID;
	private int listingID;
	private String time;
	private String annotation;
	private String listingName;
	private boolean listingExists = false;
	private String buyerID;
	private String sellerID;
	private String sellerName;
	private String senderName;
	private String recipName;
	private String message;
	private String isSenderBuyerOrSeller;
	private boolean uploadSuccess = false;
	
	public int getListingID() {
		return listingID;
	}
	
	public void setListingID(int n) {
		listingID = n;
	}
	
	public void setListingExists() {
		listingExists = true;
	}
	
	public boolean getListingExists() {
		return listingExists;
	}
	
	public void setBuyerID(String s) {
		buyerID = s;
	}
	
	public String getBuyerID() {
		return buyerID;
	}
	
	public void setSellerID(String s) {
		sellerID = s;
	}
	
	public String getSellerID() {
		return sellerID;
	}
	
	public void setMsg(String s) {
		message = s;
	}
	
	public String getMsg() {
		return message;
	}
	
	public void setListingName(String s) {
		listingName = s;
	}
	
	public String getListingName() {
		return listingName;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String s) {
		sellerName = s;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getRecipName() {
		return recipName;
	}

	public void setRecipName(String recipName) {
		this.recipName = recipName;
	}

	public String getAnnotation() {
		return annotation;
	}

	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}

	public boolean getUploadSuccess() {
		return uploadSuccess;
	}

	public void setUploadSuccess() {
		uploadSuccess = true;
	}

	public int getChainID() {
		return chainID;
	}

	public void setChainID(int chainID) {
		this.chainID = chainID;
	}

	public String getIsSenderBuyerOrSeller() {
		return isSenderBuyerOrSeller;
	}

	public void setIsSenderBuyerOrSeller(String isSenderBuyerOrSeller) {
		this.isSenderBuyerOrSeller = isSenderBuyerOrSeller;
	}

}
