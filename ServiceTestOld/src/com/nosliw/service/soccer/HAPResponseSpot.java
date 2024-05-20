package com.nosliw.service.soccer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPResponseSpot extends HAPExecutableImp{

	@HAPAttribute
	public static final String PLAYERS = "players";

	@HAPAttribute
	public static final String VACANT = "vacant";

	private List<String> m_players;
	
	private boolean m_vacant;
	
	public HAPResponseSpot() {
		this.m_players = new ArrayList<String>();
		this.m_vacant = false;
	}
	
	public void addPlayer(String player) {   this.m_players.add(player);   }
	
	public void setVacant() {   this.m_vacant = true;   }
	public boolean getVacant() {  return this.m_vacant;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PLAYERS, HAPUtilityJson.buildJson(this.m_players, HAPSerializationFormat.JSON));
		
		jsonMap.put(VACANT, this.m_vacant+"");
		typeJsonMap.put(VACANT, Boolean.class);
	}
	
}
