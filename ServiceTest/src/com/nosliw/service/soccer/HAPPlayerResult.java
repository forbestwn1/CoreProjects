package com.nosliw.service.soccer;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPBasicUtility;

public class HAPPlayerResult {

	private Set<HAPPlayerInfo> m_playerInfos;
	
	private String m_name;
	
	public HAPPlayerResult(String name, Set<HAPPlayerInfo> playerInfos) {
		this.m_name = name;
		this.m_playerInfos = playerInfos;
	}

	public String getPlayerName() {  return this.m_name;  }
	
	public Set<HAPPlayerInfo> getRelatedPlayerInfos(){   return this.m_playerInfos;   }
	
	public String emailStr() {
		StringBuffer out = new StringBuffer();
		Set<String> emails = new HashSet<String>();
		for(HAPPlayerInfo playerInfo : this.m_playerInfos) {
			if(!HAPBasicUtility.isStringEmpty(playerInfo.getEmail())) {
				emails.add(playerInfo.getEmail());
			}
		}
		return emails.toString();
	}
	
}
