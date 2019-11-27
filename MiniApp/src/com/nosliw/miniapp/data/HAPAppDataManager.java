package com.nosliw.miniapp.data;

import com.nosliw.miniapp.entity.HAPOwnerInfo;

//interface for manage app data
public interface HAPAppDataManager {

	//get all app data belong to owner
	public HAPAppDataInfoContainer getAppData(HAPOwnerInfo ownerInfo);

	//get app data by name
	public void getAppData(HAPAppDataInfoContainer appDataInfos);

	//update app data
	public void updateAppData(HAPAppDataInfoContainer appDataInfos);
}
