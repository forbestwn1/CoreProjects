package com.nosliw.app.servlet;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.nosliw.data.core.imp.io.HAPDBSource;

public class HAPAppManager {

	private HAPDataAccess m_dataAccess;
	
	public HAPAppManager() {
		this.m_dataAccess = new HAPDataAccess(HAPDBSource.getDefaultDBSource());
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

	public HAPMiniAppInstance getMiniAppInstance(String userId) {
		return this.getMyRealtorAppInfo();
	}
	
	private HAPMiniAppInstance getMyRealtorAppInfo() {
		HAPMiniAppInstance out = new HAPMiniAppInstance();

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
	
	private List<HAPMiniAppInstance> getUserMiniAppInfos(String userId) {
		List<HAPMiniAppInstance> out = new ArrayList<HAPMiniAppInstance>();
		out.add(new HAPMiniAppInstance("id1", "app1"));
		out.add(new HAPMiniAppInstance("id2", "app2"));
		out.add(new HAPMiniAppInstance("id3", "app3"));
		out.add(new HAPMiniAppInstance("id4", "app4"));
		
		return out;
	}

}
