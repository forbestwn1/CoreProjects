package com.nosliw.miniapp;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.io.HAPDBSource;
import com.nosliw.miniapp.definition.HAPDefinitionMiniApp;
import com.nosliw.miniapp.definition.HAPDefinitionMiniAppModuleEntry;
import com.nosliw.miniapp.definition.HAPDefinitionMiniAppUIEntry;
import com.nosliw.miniapp.instance.HAPInstanceMiniAppUIEntry;
import com.nosliw.miniapp.user.HAPUser;
import com.nosliw.miniapp.user.HAPUserInfo;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.module.HAPDefinitionUIModule;
import com.nosliw.uiresource.module.HAPInstanceUIModule;

public class HAPAppManager {

	private HAPDataAccess m_dataAccess;
	
	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPAppManager() {
		this.m_dataAccess = new HAPDataAccess(HAPDBSource.getDefaultDBSource());
	}
	
	public HAPDefinitionMiniApp getMinAppDefinition(String minAppDefId) {
		String file = HAPFileUtility.getMiniAppFolder()+minAppDefId+".res";
		HAPDefinitionMiniApp out = (HAPDefinitionMiniApp)HAPSerializeManager.getInstance().buildObject(HAPDefinitionMiniApp.class.getName(), new JSONObject(HAPFileUtility.readFile(new File(file))), HAPSerializationFormat.JSON);
		out.setId(minAppDefId);
		return out;
	}
	
	public HAPUserInfo createUser() {
		HAPUserInfo out = new HAPUserInfo();
		HAPUser user = this.m_dataAccess.createUser();
		out.setUser(user);
		this.m_dataAccess.createSampleDataForUser(user.getId());
		return out;
	}
	
	public HAPUserInfo getUserInfo(String id) {
		HAPUserInfo out = null;
		HAPUser user = this.m_dataAccess.getUserById(id);
		if(user!=null) {
			out = new HAPUserInfo();
			user.addGroups(this.m_dataAccess.getUserGroups(user.getId()));
			out.setUser(user);
			this.m_dataAccess.updateUserInfoWithMiniApp(out);
		}
		
		return out;
	}

	public HAPInstanceMiniAppUIEntry getMiniAppInstanceUIEntiry(String userId, String miniAppId, String uiEntry) {
		HAPInstanceMiniAppUIEntry out = new HAPInstanceMiniAppUIEntry();
		
		HAPDefinitionMiniApp minAppDef = this.getMinAppDefinition(miniAppId);
		HAPDefinitionMiniAppUIEntry miniAppUIEntry = minAppDef.getUIEntry(uiEntry);
		
		Map<String, HAPDefinitionMiniAppModuleEntry> moduleEntries = miniAppUIEntry.getUIModuleEntries();
		for(String entryName : moduleEntries.keySet()) {
			HAPDefinitionMiniAppModuleEntry moduleEntryDef = moduleEntries.get(entryName);
			HAPInstanceUIModule uiModuleInstance = this.m_uiResourceMan.getUIModuleInstance(minAppDef.getModuleIdByName(moduleEntryDef.getModule()), moduleEntryDef.getEntry());
			out.addUIModuleInstance(entryName, uiModuleInstance);
		}
		
		this.m_dataAccess.updateInstanceMiniAppUIEntry(out, userId, miniAppId, uiEntry);
		
		return out;
	}
	
	
/*	
	public HAPInstanceMiniAppUIEntry getMiniAppInstance(String instanceId) {
		return this.getMyRealtorAppInfo(instanceId);
	}
	
	private HAPInstanceMiniAppUIEntry getMyRealtorAppInfo(String instanceId) {
		HAPInstanceMiniAppUIEntry out = new HAPInstanceMiniAppUIEntry();
		out.setId(instanceId);

		HAPMiniAppSetting setting = new HAPMiniAppSetting();
		
		HAPUIModule111 settingUIModule = new HAPUIModule111();
		settingUIModule.addUiResource("Example_App_Query_MyRealtor_mobile");
		setting.setUIModule(settingUIModule);
		
		JSONObject dataValue = new JSONObject();
		dataValue.put("value", "Private");
		dataValue.put("optionsId", "schoolType");

		JSONObject data = new JSONObject();
		data.put("value", dataValue);
		data.put("dataTypeId", "test.options;1.0.0");

		JSONObject parmAppData = new JSONObject();
		parmAppData.put("value", data);
		parmAppData.put("dataTypeInfo", "appdata");
		
		setting.addParm("schoolType", parmAppData);
		
		out.setSetting(setting);
		return out;
	}
	
	private List<HAPInstanceMiniAppUIEntry> getUserMiniAppInfos(String userId) {
		List<HAPInstanceMiniAppUIEntry> out = new ArrayList<HAPInstanceMiniAppUIEntry>();
		out.add(new HAPInstanceMiniAppUIEntry("id1", "app1"));
		out.add(new HAPInstanceMiniAppUIEntry("id2", "app2"));
		out.add(new HAPInstanceMiniAppUIEntry("id3", "app3"));
		out.add(new HAPInstanceMiniAppUIEntry("id4", "app4"));
		
		return out;
	}
*/
}
