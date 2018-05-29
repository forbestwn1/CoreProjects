package com.nosliw.miniapp.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPUserInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String USER = "user";

	@HAPAttribute
	public static String MINIAPPS = "miniApps";
	
	@HAPAttribute
	public static String GROUPMINIAPP = "groupMiniApp";

	private HAPUser m_user;
	
	private List<HAPUserMiniAppInfo> m_miniApps;
	
	private List<HAPUserGroupMiniApp> m_groupMiniApp;
	
	public HAPUserInfo() {
		this.m_miniApps = new ArrayList<HAPUserMiniAppInfo>();
		this.m_groupMiniApp = new ArrayList<HAPUserGroupMiniApp>();
	}
	
	public HAPUser getUser() {  return this.m_user;   }
	public void setUser(HAPUser user) {   this.m_user = user;    }
	
	public void addMiniApps(List<HAPUserMiniAppInfo> miniApps) {	this.m_miniApps.addAll(miniApps);	}
	public void addMiniApp(HAPUserMiniAppInfo miniApp) {	this.m_miniApps.add(miniApp);	}
	
	public void addGroupMiniApps(HAPUserGroupMiniApp groupMiniApp) {this.m_groupMiniApp.add(groupMiniApp);}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(USER, HAPJsonUtility.buildJson(m_user, HAPSerializationFormat.JSON));
		jsonMap.put(MINIAPPS, HAPJsonUtility.buildJson(m_miniApps, HAPSerializationFormat.JSON));
		jsonMap.put(GROUPMINIAPP, HAPJsonUtility.buildJson(m_groupMiniApp, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject userJsonObj = jsonObj.optJSONObject(USER);
		if(userJsonObj!=null) {
			this.m_user = (HAPUser)HAPSerializeManager.getInstance().buildObject(HAPUser.class.getName(), userJsonObj, HAPSerializationFormat.JSON);
		}
		this.m_miniApps = HAPSerializeUtility.buildListFromJsonArray(HAPUserMiniAppInfo.class.getName(), jsonObj.optJSONArray(MINIAPPS));
		this.m_groupMiniApp = HAPSerializeUtility.buildListFromJsonArray(HAPUserGroupMiniApp.class.getName(), jsonObj.optJSONArray(GROUPMINIAPP));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}	
}
