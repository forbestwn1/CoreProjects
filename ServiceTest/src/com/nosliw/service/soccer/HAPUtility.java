package com.nosliw.service.soccer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.system.HAPSystemUtility;
import com.nosliw.miniapp.data.HAPAppDataInfo;
import com.nosliw.miniapp.data.HAPAppDataInfoContainer;

public class HAPUtility {

	public static HAPPlayerInfo buildPlayerInfo(HAPAppDataInfoContainer appDataInfoContainer) {
		HAPPlayerInfo out = null;
		for(HAPAppDataInfo appDataInfo : appDataInfoContainer.getDatas()) {
			if("playerInfo".equals(appDataInfo.getName())) {
				return buildPlayerInfo(appDataInfo);
			}
		}
		return out;
	}
	
	public static HAPPlayerInfo buildPlayerInfo(HAPAppDataInfo appDataInfo) {
		JSONArray data = appDataInfo.getData();
		JSONObject ele = data.getJSONObject(0);
		JSONObject player = ele.getJSONObject("data").getJSONObject("player");
		String playerName = player.getJSONObject("name").getString("value");
		String email = player.getJSONObject("email").getString("value");
		return new HAPPlayerInfo(appDataInfo.getOwnerInfo().getUserId(), playerName, email);
	}
	
	public static String getBaseFolder() {	return HAPSystemUtility.getAppDataFolder()+"/soccerforfun/";}
	public static String getOriginalLineupFile() {   return getBaseFolder()+"lineup.json";    }
	public static String getLineupFile() {   return getBaseFolder()+"lineup/lineup001.json";    }
	public static String getPlayerInfoFile() {   return getBaseFolder()+"players.json";    }

}
