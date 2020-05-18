package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPStoryElementImp extends HAPEntityInfoImp implements HAPStoryElement{

	private String m_type;
	
	private HAPStatus m_status;
	
	private Object m_entity;

	public HAPStoryElementImp() {
		this.m_status = new HAPStatus();
	}

	public HAPStoryElementImp(String type) {
		this();
		this.m_type = type;
	}
	
	@Override
	public String getType() {  return this.m_type; }

	@Override
	public HAPStatus getStatus() {  return this.m_status;  }

	@Override
	public Object getEntity() {  return this.m_entity;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_type = jsonObj.getString(TYPE);
		this.m_entity = jsonObj.opt(ENTITY);
		this.m_status.buildObject(jsonObj.optJSONObject(STATUS), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(STATUS, HAPJsonUtility.buildJson(this.m_status, HAPSerializationFormat.JSON));
		jsonMap.put(ENTITY, HAPJsonUtility.buildJson(this.m_entity, HAPSerializationFormat.JSON));
	}
	
}
