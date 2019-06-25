package com.nosliw.service.soccer;

public class HAPActionResult {

	private String m_affectedPlayer;
	
	private HAPPlayerStatus m_playerStatus;
	
	public HAPActionResult(HAPPlayerStatus playerStatus, String affectedPlayer) {
		this.m_playerStatus = playerStatus;
		this.m_affectedPlayer = affectedPlayer;
	}
	
	public String getAffectedPlayer() {    return this.m_affectedPlayer;     }
	
	public HAPPlayerStatus getPlayerStatus() {   return this.m_playerStatus;    }
	
}
