package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPSpot extends HAPExecutableImp{

	@HAPAttribute
	public static final String PLAYERS = "players";

	private List<String> m_players;
	
	public HAPSpot(String player) {
		this();
		this.m_players.add(player);
	}

	public HAPSpot() {
		this.m_players = new ArrayList<String>();
	}

	public String getOriginalPlayer() {return this.m_players.get(0); }
	public List<String> getPlayers(){   return this.m_players;   }
	public String getPlayer() {   return this.m_players.get(this.m_players.size()-1);    }
	
	public void addPlayer(String player) {   this.m_players.add(player);   }
	
	public String replacedBy() {   
		if(this.m_players.size()<=1)  return null;
		else return this.getPlayer();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PLAYERS, HAPJsonUtility.buildArrayJson(m_players.toArray(new String[0])));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONArray playArray = jsonObj.optJSONArray(PLAYERS);
		for(int i=0; i<playArray.length(); i++) {
			this.m_players.add(playArray.getString(i));
		}
		return true;  
	}
}
