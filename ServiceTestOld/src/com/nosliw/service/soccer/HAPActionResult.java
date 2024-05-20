package com.nosliw.service.soccer;

import java.util.List;

public class HAPActionResult {

	private List<String> m_affectedPlayers;
	
	private HAPPlayerStatus m_playerStatus;
	
	public HAPActionResult(HAPPlayerStatus playerStatus, List<String> affectedPlayers) {
		this.m_playerStatus = playerStatus;
		this.m_affectedPlayers = affectedPlayers;
	}
	
	public void addAffectedPlayer(String player) {    this.m_affectedPlayers.add(player);    }
	public List<String> getAffectedPlayer() {    return this.m_affectedPlayers;     }
	
	public HAPPlayerStatus getPlayerStatus() {   return this.m_playerStatus;    }
	
}
