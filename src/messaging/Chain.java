package messaging;

public class Chain {
	
	private int chainID;
	private String listingName;
	private String buyerName;
	private String sellerName;
	private boolean containsNewMsg;
	private int largestMsgID;
	
	public String getListingName() {
		return listingName;
	}
	public void setListingName(String listingName) {
		this.listingName = listingName;
	}
	public String getSellerName() {
		return sellerName;
	}
	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public boolean isContainsNewMsg() {
		return containsNewMsg;
	}
	public void setContainsNewMsg(boolean containsNewMsg) {
		this.containsNewMsg = containsNewMsg;
	}
	public int getLargestMsgID() {
		return largestMsgID;
	}
	public void setLargestMsgID(int largestMsgID) {
		this.largestMsgID = largestMsgID;
	}
	public int getChainID() {
		return chainID;
	}
	public void setChainID(int chainID) {
		this.chainID = chainID;
	}

}
