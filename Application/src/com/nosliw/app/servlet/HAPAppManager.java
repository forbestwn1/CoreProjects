package com.nosliw.app.servlet;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPDataWrapper;
import com.nosliw.uiresource.HAPUIResourceManager;

public class HAPAppManager {

	private HAPUIResourceManager m_uiResourceManager;
	
	public HAPAppManager(HAPUIResourceManager resourceMan) {
		this.m_uiResourceManager = resourceMan;
	}
	
	public HAPUserInfo getUserInfo(String id) {
		HAPUserInfo out = new HAPUserInfo();
		out.setId(id);
		out.addMiniAppInfo(this.getUserMiniAppInfos(id));
		return out;
	}

	public HAPMiniAppInfo getMiniAppInfo(String id) {
		return this.getMyRealtorAppInfo();
	}
	
	private HAPMiniAppInfo getMyRealtorAppInfo() {
		HAPMiniAppInfo out = new HAPMiniAppInfo();

		HAPMiniAppSetting setting = new HAPMiniAppSetting();
		
		HAPUIModule settingUIModule = new HAPUIModule();
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
	
	private List<HAPMiniAppInfo> getUserMiniAppInfos(String userId) {
		List<HAPMiniAppInfo> out = new ArrayList<HAPMiniAppInfo>();
		out.add(new HAPMiniAppInfo("id1", "app1"));
		out.add(new HAPMiniAppInfo("id2", "app2"));
		out.add(new HAPMiniAppInfo("id3", "app3"));
		out.add(new HAPMiniAppInfo("id4", "app4"));
		
		return out;
	}

}
