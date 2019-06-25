package com.nosliw.miniapp;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.io.HAPDBSource;
import com.nosliw.miniapp.entity.HAPMiniAppSettingData;
import com.nosliw.miniapp.entity.HAPOwnerInfo;
import com.nosliw.miniapp.entity.HAPUser;
import com.nosliw.miniapp.entity.HAPUserInfo;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.application.HAPDefinitionApp;

public class HAPAppManager {

	private HAPDataAccess m_dataAccess;
	
	private HAPUIResourceManager m_uiResourceMan;
	
	private Map<String, Map<String, List<HAPAppDataProcessor>>> m_appDataProcessors;
	
	public HAPAppManager(HAPUIResourceManager resourceMan) {
		this.m_dataAccess = new HAPDataAccess(HAPDBSource.getDefaultDBSource());
		this.m_uiResourceMan = resourceMan;
		this.m_appDataProcessors = new LinkedHashMap<String, Map<String, List<HAPAppDataProcessor>>>();
	}
	
	public HAPDefinitionApp getMinAppDefinition(String minAppDefId) {
		String file = HAPFileUtility.getMiniAppFolder()+minAppDefId+".res";
		HAPDefinitionApp out = (HAPDefinitionApp)HAPSerializeManager.getInstance().buildObject(HAPDefinitionApp.class.getName(), new JSONObject(HAPFileUtility.readFile(new File(file))), HAPSerializationFormat.JSON);
		out.setId(minAppDefId);
		return out;
	}
	
	public HAPUserInfo createUser() {
		HAPUser user = this.m_dataAccess.createUser();
		this.m_dataAccess.createSampleDataForUser(user.getId());
		return getUserInfo(user.getId());
	}
	
	public HAPUserInfo getUserInfo(String id) {
		HAPUserInfo out = null;
		HAPUser user = this.m_dataAccess.getUserById(id);
		if(user!=null) {
			out = new HAPUserInfo();
			out.setUser(user);
			out.addGroups(this.m_dataAccess.getUserGroups(user.getId()));
			this.m_dataAccess.updateUserInfoWithMiniApp(out);
		}
		return out;
	}

	public HAPMiniAppSettingData getAppData(HAPOwnerInfo ownerInfo) {
		return this.m_dataAccess.getSettingData(ownerInfo);
	}

	public HAPMiniAppSettingData getAppData(HAPOwnerInfo ownerInfo, String[] dataNames) {
		return this.m_dataAccess.getSettingData(ownerInfo, dataNames);
	}

	public HAPMiniAppSettingData updateAppData(HAPMiniAppSettingData miniAppSettingData) {
		List<HAPAppDataProcessor> processores = this.findProcessors(miniAppSettingData.getOwnerInfo());
		if(processores!=null) {
			for(HAPAppDataProcessor processor : processores) {
				processor.updateSettingData(miniAppSettingData);
			}
		}
		return this.m_dataAccess.updateSettingData(miniAppSettingData);
	}

	private List<HAPAppDataProcessor> findProcessors(HAPOwnerInfo ownerInfo) {
		Map<String, List<HAPAppDataProcessor>> processByName = this.m_appDataProcessors.get(ownerInfo.getComponentType());
		if(processByName==null)  return null;
		return processByName.get(ownerInfo.getComponentId());
	}
	
	public void addProcessor(HAPOwnerInfo ownerInfo, HAPAppDataProcessor appDataProcessor) {
		Map<String, List<HAPAppDataProcessor>> processByName = this.m_appDataProcessors.get(ownerInfo.getComponentType());
		if(processByName==null) {
			processByName = new LinkedHashMap<String, List<HAPAppDataProcessor>>();
			this.m_appDataProcessors.put(ownerInfo.getComponentType(), processByName);
		}
		
		List<HAPAppDataProcessor> processes = processByName.get(ownerInfo.getComponentId());
		if(processes==null) {
			processes = new ArrayList<HAPAppDataProcessor>();
			processByName.put(ownerInfo.getComponentId(), processes);
		}
		processes.add(appDataProcessor);
	}
	
	/*
	public HAPSettingData createMiniAppData(String userId, String appId, String dataName, HAPSettingData dataInfo) {
		HAPSettingData out = null;
		out = this.m_dataAccess.addSettingData(userId, appId, dataName, dataInfo);
		switch(dataInfo.getType()) {
		case HAPConstant.MINIAPPDATA_TYPE_SETTING:
			break;
		}
		return out;
	}
	
	public void deleteMiniAppData(String dataId, String dataType) {
		switch(dataType) {
		case HAPConstant.MINIAPPDATA_TYPE_SETTING:
			this.m_dataAccess.deleteSettingData(dataId);
			break;
		}
	}
	
	public HAPSettingData updateMiniAppData(String id, HAPSettingData dataInfo) {
		HAPSettingData out = null;
		switch(dataInfo.getType()) {
		case HAPConstant.MINIAPPDATA_TYPE_SETTING:
			out = this.m_dataAccess.updateSettingData(id, dataInfo);
			break;
		}
		return out;
	}
	
	public HAPMiniAppEntryInstance getMiniAppInstanceUIEntry(String userId, String miniAppId, String entry) {
		
		HAPMiniAppEntryInstance out = new HAPMiniAppEntryInstance();
		
		HAPExecutableAppEntry entryExe = m_uiResourceMan.getMiniApp(miniAppId, entry);
		out.setEntry(entryExe);
		
		HAPMiniAppSettingData settingData = this.m_dataAccess.getSettingData(userId, miniAppId);
		out.setData(settingData);
		
		return out;
	}
	*/
	
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
