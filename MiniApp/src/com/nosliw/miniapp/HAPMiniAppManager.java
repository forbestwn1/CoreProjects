package com.nosliw.miniapp;

import java.util.List;

import com.nosliw.miniapp.definition.HAPDefinitionMiniApp;
import com.nosliw.miniapp.user.HAPGroup;
import com.nosliw.miniapp.user.HAPInstanceMiniApp;
import com.nosliw.miniapp.user.HAPSettingMiniApp;
import com.nosliw.miniapp.user.HAPUser;

public interface HAPMiniAppManager {

	//get mini app definition 
	HAPDefinitionMiniApp getMiniAppDefinition(String id);

	//mini app instance
	HAPInstanceMiniApp getMiniAppInstance(String entry, String miniAppid, String userId);
	void deleteMiniAppInstance(String miniAppId);
	
	//setting related with mini app  
	List<HAPSettingMiniApp> getMiniAppSetting(String miniAppid, String userId);
	HAPSettingMiniApp addMiniAppSetting(String miniAppid, String userId, HAPSettingMiniApp setting);
	void removeMiniAppSetting(String miniAppid, String userId, HAPSettingMiniApp setting);
	void modifyMiniAppSetting(String miniAppid, String userId, HAPSettingMiniApp setting);
	
	//user
	HAPUser createUser(HAPUser user);
	HAPUser getUser(String userId);
	void modifyUser(HAPUser user);
	void deleteUser(String userId);
	
	//group
	HAPGroup createGroup(HAPGroup group, String userId);
	HAPGroup getGroup(String groupId);
	void modifyGroup(HAPGroup group);
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
