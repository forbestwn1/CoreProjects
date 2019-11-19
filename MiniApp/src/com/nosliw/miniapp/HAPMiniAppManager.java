package com.nosliw.miniapp;

import java.util.List;

import com.nosliw.miniapp.data.HAPSettingData;
import com.nosliw.miniapp.entity.HAPGroup;
import com.nosliw.miniapp.entity.HAPUser;
import com.nosliw.uiresource.application.HAPDefinitionApp;
import com.nosliw.uiresource.application.HAPExecutableAppEntry;

public interface HAPMiniAppManager {

	//get mini app definition 
	HAPDefinitionApp getMiniAppDefinition(String miniAppId);

	//mini app instance
	HAPExecutableAppEntry getMiniAppEntry(String entry, String miniAppid, String userId);

	void deleteMiniApp(String miniAppId);

	//setting related with mini app  
	List<HAPSettingData> getMiniAppSetting(String miniAppid, String userId);
	HAPSettingData addMiniAppSetting(String miniAppid, String userId, HAPSettingData setting);
	void removeMiniAppSetting(String miniAppid, String userId, String version);
	void updateMiniAppSetting(String miniAppid, String userId, HAPSettingData setting);
	
	//user
	HAPUser createUser(HAPUser user);
	HAPUser getUser(String userId);
	void updateUser(HAPUser user);
	void deleteUser(String userId);
	
	//group
	HAPGroup createGroup(HAPGroup group, String userId);
	HAPGroup getGroup(String groupId);
	void updateGroup(HAPGroup group);
	void deleteGroup(String groupId);
	void addMiniAppToGroup(String miniAppId, String groupId);
	void removeMiniAppFromGroup(String miniAppId, String groupId);
	
	//user group
	void assignGroupToUser(String group, String user);
	void removeGroupFromUser(String group, String user);
	
	//user miniapp
	void assignMiniAppToUser(String miniAppId, String userId);
	void removeMiniAppFromUser(String miniAppId, String userId);
	
}
