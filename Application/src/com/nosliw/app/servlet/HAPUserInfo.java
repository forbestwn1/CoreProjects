package com.nosliw.app.servlet;

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
	public static String MINIAPPINSTANCES = "miniAppInstances";
	
	@HAPAttribute
	public static String GROUPMINIAPPINSTANCES = "groupMiniAppInstances";

	private HAPUser m_user;
	
	private List<HAPMiniAppInstance> m_miniAppInstances;
	
	private List<HAPUserGroupMiniAppInstances> m_groupMiniAppInstances;
	
	
	public HAPUserInfo() {
		this.m_miniAppInstances = new ArrayList<HAPMiniAppInstance>();
		this.m_groupMiniAppInstances = new ArrayList<HAPUserGroupMiniAppInstances>();
	}
	
	public HAPUser getUser() {  return this.m_user;   }
	public void setUser(HAPUser user) {   this.m_user = user;    }
	
	public void addMiniAppInstances(List<HAPMiniAppInstance> miniAppInstances) {	this.m_miniAppInstances.addAll(miniAppInstances);	}
	public void addMiniAppInstance(HAPMiniAppInstance miniAppInstance) {	this.m_miniAppInstances.add(miniAppInstance);	}
	
	public void addGroupMiniAppInstances(HAPUserGroupMiniAppInstances groupMiniAppInstance) {this.m_groupMiniAppInstances.add(groupMiniAppInstance);}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(USER, HAPJsonUtility.buildJson(m_user, HAPSerializationFormat.JSON));
		jsonMap.put(MINIAPPINSTANCES, HAPJsonUtility.buildJson(m_miniAppInstances, HAPSerializationFormat.JSON));
		jsonMap.put(GROUPMINIAPPINSTANCES, HAPJsonUtility.buildJson(m_groupMiniAppInstances, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		JSONObject userJsonObj = jsonObj.optJSONObject(USER);
		if(userJsonObj!=null) {
			this.m_user = (HAPUser)HAPSerializeManager.getInstance().buildObject(HAPUser.class.getName(), userJsonObj, HAPSerializationFormat.JSON);
		}
		this.m_miniAppInstances = HAPSerializeUtility.buildListFromJsonArray(HAPMiniAppInstance.class.getName(), jsonObj.optJSONArray(MINIAPPINSTANCES));
		this.m_groupMiniAppInstances = HAPSerializeUtility.buildListFromJsonArray(HAPUserGroupMiniAppInstances.class.getName(), jsonObj.optJSONArray(GROUPMINIAPPINSTANCES));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}	
}
