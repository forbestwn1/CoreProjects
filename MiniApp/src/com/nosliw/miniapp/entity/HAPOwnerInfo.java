package com.nosliw.miniapp.entity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPOwnerInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String USERID = "userId";
	
	@HAPAttribute
	public static final String COMPONENTID = "componentId";

	@HAPAttribute
	public static final String COMPONENTTYPE = "componentType";

	private String m_userId;
	
	private String m_componentId;
	
	private String m_componentType;

	public HAPOwnerInfo() {}

	public HAPOwnerInfo(String userId, String componentId, String componentType) {
		this.m_userId = userId;
		this.m_componentId = componentId;
		this.m_componentType = componentType;
	}

	public String getUserId() {   return this.m_userId;    }
	public String getComponentId() {   return this.m_componentId;   }
	public String getComponentType() {   return this.m_componentType;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(USERID, this.m_userId);
		jsonMap.put(COMPONENTID, this.m_componentId);
		jsonMap.put(COMPONENTTYPE, this.m_componentType);
	}	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByFullJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_userId = (String)jsonObj.opt(USERID);
		this.m_componentId = (String)jsonObj.opt(COMPONENTID);
		this.m_componentType = (String)jsonObj.opt(COMPONENTTYPE);
		return true;
	}
	
}
