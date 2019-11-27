package com.nosliw.service.soccer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.miniapp.data.HAPAppDataInfoContainer;

public class HAPUtility {

	public static HAPPlayerInfo buildPlayerInfo(HAPAppDataInfoContainer miniAppSettingData) {
		JSONArray data = (JSONArray)miniAppSettingData.getDatas().get("playerInfo").getData();
		JSONObject ele = data.getJSONObject(0);
		JSONObject player = ele.getJSONObject("data").getJSONObject("player");
		String playerName = player.getJSONObject("name").getString("value");
		String email = player.getJSONObject("email").getString("value");
		return new HAPPlayerInfo(miniAppSettingData.getOwnerInfo().getUserId(), playerName, email);
	}
	
	public static String getBaseFolder() {	return HAPSystemUtility.getAppDataFolder()+"/soccerforfun/";}
	public static String getOriginalLineupFile() {   return getBaseFolder()+"lineup.json";    }
	public static String getLineupFile() {   return getBaseFolder()+"lineup/lineup001.json";    }
	public static String getPlayerInfoFile() {   return getBaseFolder()+"players.json";    }

}
