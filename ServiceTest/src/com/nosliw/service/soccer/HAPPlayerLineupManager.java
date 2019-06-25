package com.nosliw.service.soccer;

public class HAPPlayerLineupManager {

	static private HAPPlayerLineupManager m_instance;
	private HAPPlayerLineupManager() {}
	
	
	public static HAPPlayerLineupManager getInstance() {
		if(m_instance==null) {
			m_instance = new HAPPlayerLineupManager();
		}
		return m_instance;
	}
	
	public HAPPlayerLineup getLineup() {
		HAPPlayerLineup out = this.readLineUp();
		if(out==null) {
			out = this.getOrignalLineup();
			this.writeLineUp(out);
		}
		return out;
	}
	
	public HAPActionResult updateLineUp(String player, String action) {
		HAPPlayerLineup lineup = this.getLineup();
		HAPActionResult out = lineup.action(player, action);
		this.writeLineUp(lineup);
		return out;
	}
	
	private HAPPlayerLineup getOrignalLineup(){
		
	}
	
	private HAPPlayerLineup readLineUp() {
		
	}
	
	private void writeLineUp(HAPPlayerLineup lineup) {
		
	}
}
