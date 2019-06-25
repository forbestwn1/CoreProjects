package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.List;

public class HAPSpot {

	private List<String> m_players;
	
	public HAPSpot() {
		this.m_players = new ArrayList<String>();
	}

	public String getPlayer() {   return this.m_players.get(this.m_players.size()-1);    }
	
	public void addPlayer(String player) {   this.m_players.add(player);   }
	
}
