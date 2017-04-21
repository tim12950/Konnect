package messaging;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

import messaging.Chain;

public class SortChains {
	
public static ArrayList<Chain> sortChains(Connection c, ArrayList<Chain> chains) throws SQLException{
		
		ArrayList<Chain> chainsWithUnreadMsgs = new ArrayList<Chain>();
		ArrayList<Chain> sortedChainsWithUnreadMsgs = new ArrayList<Chain>();
		ArrayList<Chain> chainsWithNoUnreadMsgs = new ArrayList<Chain>();
		ArrayList<Chain> sortedChainsWithNoUnreadMsgs = new ArrayList<Chain>();
		ArrayList<Chain> sortedChains = new ArrayList<Chain>();
		
		int length = chains.size();
		
		for (int i = 0; i < length; i++) {
			if (chains.get(i).isContainsNewMsg()) {
				chainsWithUnreadMsgs.add(chains.get(i));
			}
			else {
				chainsWithNoUnreadMsgs.add(chains.get(i));
			}
		}
		
		int otherlength = chainsWithUnreadMsgs.size();
		
		if (otherlength > 0) {
			ArrayList<Integer> msgids = new ArrayList<Integer>();
			for (int i = 0; i < otherlength; i++) {
				msgids.add(chainsWithUnreadMsgs.get(i).getLargestMsgID());
			}
			Collections.sort(msgids);
			Collections.reverse(msgids);
			
			for (int i = 0; i < otherlength; i++) {
				for (int j = 0; j < otherlength; j++) {
					if (chainsWithUnreadMsgs.get(j).getLargestMsgID() == msgids.get(i)) {
						sortedChainsWithUnreadMsgs.add(chainsWithUnreadMsgs.get(j));
					}
				}
			}
		}
		
		int anotherlength = chainsWithNoUnreadMsgs.size();
		
		if (anotherlength > 0) {
			ArrayList<Integer> msgids = new ArrayList<Integer>();
			for (int i = 0; i < anotherlength; i++) {
				msgids.add(chainsWithNoUnreadMsgs.get(i).getLargestMsgID());
			}
			Collections.sort(msgids);
			Collections.reverse(msgids);
			
			for (int i = 0; i < anotherlength; i++) {
				for (int j = 0; j < anotherlength; j++) {
					if (chainsWithNoUnreadMsgs.get(j).getLargestMsgID() == msgids.get(i)) {
						sortedChainsWithNoUnreadMsgs.add(chainsWithNoUnreadMsgs.get(j));
					}
				}
			}
		}
		
		for (int i = 0; i < otherlength; i++) {
			sortedChains.add(sortedChainsWithUnreadMsgs.get(i));
		}
		
		for (int i = 0; i < anotherlength; i++) {
			sortedChains.add(sortedChainsWithNoUnreadMsgs.get(i));
		}
		
		return sortedChains;
		
	}

}
