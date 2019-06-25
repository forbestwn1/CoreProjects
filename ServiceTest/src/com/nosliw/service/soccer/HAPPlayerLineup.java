package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HAPPlayerLineup {

	public final static String STATUS_LINEUP = "lineup";
	public final static String STATUS_WAITINGLIST = "waitinglist";
	public final static String STATUS_PROVIDER = "provider";
	public final static String STATUS_NOTHING = "nothing";

	public final static String ACTION_OFFER = "offer";
	public final static String ACTION_WITHDRAW = "withdraw";
	public final static String ACTION_LOOKINGFOR = "lookingfor";

	private static Map<String, List<String>> m_validActions = new LinkedHashMap<String, List<String>>();
	static {
		m_validActions.put(STATUS_LINEUP, Arrays.asList(new String[] {ACTION_OFFER}));
		m_validActions.put(STATUS_WAITINGLIST, Arrays.asList(new String[] {ACTION_WITHDRAW}));
		m_validActions.put(STATUS_PROVIDER, Arrays.asList(new String[] {ACTION_WITHDRAW}));
		m_validActions.put(STATUS_NOTHING, Arrays.asList(new String[] {ACTION_LOOKINGFOR}));
	}
	
	private List<String> m_waitingList;
	
	private List<HAPSpot> m_lineUp;
	
	private List<Integer> m_vacant;
	
	public HAPPlayerLineup(List<String> lineUp) {
		this.m_waitingList = new ArrayList<String>();
		this.m_vacant = new ArrayList<Integer>();
		this.m_lineUp = new ArrayList<HAPSpot>();
	}

	public HAPActionResult action(String player, String action) {
		HAPPlayerStatus playerStatus = this.getPlayerStatus(player);
		if(playerStatus.getActions().indexOf(action)==-1)  return null;  //invalid
		
		String affectedPlayer = null;
		String status = playerStatus.getStatus();
		if(status.equals(STATUS_LINEUP)) {
			if(action.equals(ACTION_OFFER)) {
				int lineupIndex = (Integer)playerStatus.getStatusData();
				if(this.m_waitingList.size()>0) {
					//someone from waiting list replace you
					affectedPlayer = this.m_waitingList.get(0);
					this.m_lineUp.get(lineupIndex).addPlayer(affectedPlayer);
					this.m_waitingList.remove(0);
				}
				else {
					//no one replace you
					this.m_vacant.add(lineupIndex);
				}
			}
		}
		else if(status.equals(STATUS_WAITINGLIST)) {
			if(action.equals(ACTION_WITHDRAW)) {
				int waitingListIndex = (Integer)playerStatus.getStatusData();
				this.m_waitingList.remove(waitingListIndex);
			}
		}
		else if(status.equals(STATUS_PROVIDER)) {
			if(action.equals(ACTION_WITHDRAW)) {
				int vacantListIndex = (Integer)playerStatus.getStatusData();
				this.m_vacant.remove(vacantListIndex);
			}
		}
		else if(status.equals(STATUS_NOTHING)) {
			if(action.equals(ACTION_LOOKINGFOR)) {
				if(this.m_vacant.size()>0) {
					//spot available
					int lineUpIndex = this.m_vacant.get(0);
					HAPSpot spot = this.m_lineUp.get(lineUpIndex);
					affectedPlayer = spot.getPlayer();
					spot.addPlayer(player);
					this.m_vacant.remove(0);
				}
				else {
					//waiting list
					this.m_waitingList.add(player);
				}
			}
		}
		
		return new HAPActionResult(this.getPlayerStatus(player), affectedPlayer);
		
	}
	
	public HAPPlayerStatus getPlayerStatus(String player) {
		String status;
		Object statusData = null;
		
		int waitingListIndex = this.m_waitingList.indexOf(player);
		if(waitingListIndex!=-1) {
			status = STATUS_WAITINGLIST;
			statusData = new Integer(waitingListIndex);
		}
		else {
			int lineUpIndex = -1;
			for(int i=0; i<this.m_lineUp.size(); i++) {
				if(player.equals(this.m_lineUp.get(i).getPlayer())) {
					lineUpIndex = i;
					break;
				}
			}
			if(lineUpIndex!=-1) {
				int vacantIndex = this.m_vacant.indexOf(Integer.valueOf(lineUpIndex));
				if(vacantIndex==-1) {
					status = STATUS_LINEUP;
					statusData = lineUpIndex;
				}
				else {
					status = STATUS_PROVIDER;
					statusData = vacantIndex;
				}
			}
			else {
				status = STATUS_NOTHING;
			}
		}
		return new HAPPlayerStatus(status, statusData, m_validActions.get(status));
		
	}
}
