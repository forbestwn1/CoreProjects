package com.nosliw.service.soccer;

import java.io.File;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.miniapp.data.HAPMiniAppSettingData;
import com.nosliw.miniapp.entity.HAPOwnerInfo;

public class HAPPlayerManager {

	private static HAPPlayerManager m_instance;
	
	public static HAPPlayerManager getInstance() {
		if(m_instance==null)   m_instance = new HAPPlayerManager();
		return m_instance;
	}

	//playerName -- userId
	private Map<String, Set<String>> m_playerInfos;

	//update (new/modify) playerInfo 
	public void updatePlayerInfo(HAPPlayerInfoCore playerInfo) {
		//remove playerInfo by id first
		for(String name : this.getPlayerInfos().keySet()) {
			Set<String> ids = this.getPlayerInfos().get(name);
			if(ids.contains(playerInfo.getUserId())) {
				ids.remove(playerInfo.getUserId());
				break;
			}
		}
		
		Set<String> ids = getPlayerInfos().get(playerInfo.getName());
		if(ids==null) {
			ids = new HashSet<String>();
			this.getPlayerInfos().put(playerInfo.getName(), ids);
		}
		ids.add(playerInfo.getUserId());
		//persist the change
		this.savePlayerInfos();
	}

	public HAPPlayerInfo getPlayerInfoByUserId(String userId) {
		HAPMiniAppSettingData allData = HAPAppDataProcessorImp.m_appDataMan.getAppData(this.getOwnerInfo(userId));
		return HAPUtility.buildPlayerInfo(allData);
	}

	public Set<HAPPlayerInfo> getPlayerInfo(String playerName) {
		Set<HAPPlayerInfo> out = new HashSet<HAPPlayerInfo>();
		Set<String> ids = this.getPlayerInfos().get(playerName);
		for(String id : ids) {
			out.add(this.getPlayerInfoByUserId(id));
		}
		return out;
	}

	private void savePlayerInfos() {
		String content = HAPJsonUtility.buildJson(this.getPlayerInfos(), HAPSerializationFormat.JSON);
		HAPFileUtility.writeFile(HAPUtility.getPlayerInfoFile(), HAPJsonUtility.formatJson(content));
	}

	private HAPOwnerInfo getOwnerInfo(String userId) {
		return new HAPOwnerInfo(userId, "SoccerForFun", "group");
	}
	
	private Map<String, Set<String>> getPlayerInfos(){
		if(this.m_playerInfos==null) {
			this.m_playerInfos = this.readPlayerInfos();
		}
		return this.m_playerInfos;
	}
	
	private Map<String, Set<String>> readPlayerInfos(){
		Map<String, Set<String>> out = new LinkedHashMap<String, Set<String>>();
		if(!new File(HAPUtility.getPlayerInfoFile()).exists()) return out;
		JSONObject playInfoJson = new JSONObject(HAPFileUtility.readFile(HAPUtility.getPlayerInfoFile()));
		for(Object key : playInfoJson.keySet()) {
			Set<String> ids = new HashSet<String>();
			String playerName = (String)key;
			JSONArray idsArray = playInfoJson.optJSONArray(playerName);
			for(int i=0; i<idsArray.length(); i++) {
				ids.add(idsArray.getString(i));
			}
			out.put(playerName, ids);
		}
		return out;
	}
}
