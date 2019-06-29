package com.nosliw.service.soccer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPSystemUtility;

public class HAPPlayerLineupManager {

	static private HAPPlayerLineupManager m_instance;
	private HAPPlayerLineupManager() {}
	
	
	public static HAPPlayerLineupManager getInstance() {
		if(m_instance==null) {
			m_instance = new HAPPlayerLineupManager();
		}
		return m_instance;
	}
	
	public List<String> getInitialLineupPlayers(){
		List<String> players = new ArrayList<String>();
		String lineUpContent = HAPFileUtility.readFile(this.getOriginalLineupFile());
		JSONArray lineUpArray = new JSONArray(lineUpContent);
		for(int i=0; i<lineUpArray.length(); i++) players.add(lineUpArray.getString(i));
		return players;
	}

	public HAPPlayerLineup getLineup() {
		HAPPlayerLineup out = this.readLineUp();
		if(out==null) {
			out = this.getInitialLineup();
			this.writeLineUp(out);
		}
		return out;
	}
	
	public HAPActionResult updateLineUp(String player, String action) {
		HAPPlayerLineup lineup = this.getLineup();
		HAPActionResult out = null;
		if(action!=null) {
			out = lineup.action(player, action);
			this.writeLineUp(lineup);
		}
		else {
			out = new HAPActionResult(lineup.getPlayerStatus(player), null);
		}
		return out;
	}
	
	public HAPPlayerInfo getPlayerInfo(String playerName) {
		return this.getPlayerInfos().get(playerName);
	}
	
	public void addPlayerInfo(HAPPlayerInfo playerInfo) {
		Map<String, HAPPlayerInfo> playerInfos = this.getPlayerInfos();
		playerInfos.put(playerInfo.getName(), playerInfo);
		this.savePlayerInfos(playerInfos);
	}
	
	private HAPPlayerLineup getInitialLineup(){
		List<String> players = this.getInitialLineupPlayers();
		HAPPlayerLineup out = new HAPPlayerLineup(players);
		return out;
	}

	private HAPPlayerLineup readLineUp() {
		if(!new File(this.getLineupFile()).exists()) return null;
		
		HAPPlayerLineup out = new HAPPlayerLineup();
		JSONObject lineUpJson = new JSONObject(HAPFileUtility.readFile(this.getLineupFile()));
		out.buildObject(lineUpJson, HAPSerializationFormat.JSON);
		return out;
	}
	
	private void writeLineUp(HAPPlayerLineup lineup) {
		HAPFileUtility.writeFile(this.getLineupFile(), HAPJsonUtility.formatJson(lineup.toStringValue(HAPSerializationFormat.JSON)));
	}
	
	private void savePlayerInfos(Map<String, HAPPlayerInfo> playerInfos) {
		String content = HAPJsonUtility.buildJson(playerInfos, HAPSerializationFormat.JSON);
		HAPFileUtility.writeFile(this.getPlayerInfoFile(), content);
	}
	
	private Map<String, HAPPlayerInfo> getPlayerInfos(){
		Map<String, HAPPlayerInfo> out = new LinkedHashMap<String, HAPPlayerInfo>();
		if(!new File(this.getPlayerInfoFile()).exists()) return out;
		JSONObject playInfoJson = new JSONObject(HAPFileUtility.readFile(this.getPlayerInfoFile()));
		for(Object key : playInfoJson.keySet()) {
			String playerName = (String)key;
			HAPPlayerInfo playerInfo = new HAPPlayerInfo();
			playerInfo.buildObject(playInfoJson.getJSONObject(playerName), HAPSerializationFormat.JSON);
			out.put(playerName, playerInfo);
		}
		return out;
	}
	
	private String getBaseFolder() {	return HAPSystemUtility.getAppDataFolder()+"/soccerforfun/";}
	private String getOriginalLineupFile() {   return this.getBaseFolder()+"lineup.json";    }
	private String getLineupFile() {   return this.getBaseFolder()+"lineup/lineup001.json";    }
	private String getPlayerInfoFile() {   return this.getBaseFolder()+"players.json";    }
}
