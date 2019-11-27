package com.nosliw.service.soccer;

import com.nosliw.miniapp.data.HAPAppDataHandler;
import com.nosliw.miniapp.data.HAPAppDataManager;
import com.nosliw.miniapp.data.HAPAppDataInfoContainer;

public class HAPAppDataProcessorImp implements HAPAppDataHandler {

	public static HAPAppDataManager m_appDataMan;
	
	public HAPAppDataProcessorImp(HAPAppDataManager appDataMan) {
		this.m_appDataMan = appDataMan;
	}
	
	@Override
	public HAPAppDataInfoContainer updateSettingData(HAPAppDataInfoContainer miniAppSettingData) {
		HAPPlayerInfo playerInfo = HAPUtility.buildPlayerInfo(miniAppSettingData);
		HAPPlayerManager.getInstance().updatePlayerInfo(playerInfo);
		return miniAppSettingData;
	}

}
