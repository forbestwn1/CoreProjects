package com.nosliw.service.soccer;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.miniapp.HAPAppDataProcessor;
import com.nosliw.miniapp.entity.HAPMiniAppSettingData;

public class HAPAppDataProcessorImp implements HAPAppDataProcessor {

	@Override
	public HAPMiniAppSettingData updateSettingData(HAPMiniAppSettingData miniAppSettingData) {
		JSONArray data = (JSONArray)miniAppSettingData.getDatas().get("playerInfo").getData();
		JSONObject ele = data.getJSONObject(0);
		JSONObject player = ele.getJSONObject("data").getJSONObject("player");
		String playerName = player.getJSONObject("name").getString("value");
		String email = player.getJSONObject("email").getString("value");
		HAPPlayerLineupManager.getInstance().addPlayerInfo(new HAPPlayerInfo(playerName, email));
		return miniAppSettingData;
	}

}
