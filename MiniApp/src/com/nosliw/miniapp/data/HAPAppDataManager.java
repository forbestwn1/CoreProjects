package com.nosliw.miniapp.data;

import com.nosliw.miniapp.entity.HAPOwnerInfo;

//interface for manage app data
public interface HAPAppDataManager {

	//get all app data belong to owner
	public HAPMiniAppSettingData getAppData(HAPOwnerInfo ownerInfo);

	//get app data by name
	public HAPMiniAppSettingData getAppData(HAPOwnerInfo ownerInfo, String[] dataNames);

	//update app data
	public HAPMiniAppSettingData updateAppData(HAPMiniAppSettingData miniAppSettingData);
}
