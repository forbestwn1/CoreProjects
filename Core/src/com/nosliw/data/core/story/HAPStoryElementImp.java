package com.nosliw.data.core.story;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPStoryElementImp extends HAPEntityInfoImp implements HAPStoryElement{

	private String m_categary;
	
	private String m_type;
	
	private HAPStatus m_status;
	
	private Object m_entity;

	public HAPStoryElementImp() {
		this.m_status = new HAPStatus();
		this.m_entity = new JSONObject();
	}

	public HAPStoryElementImp(String categary) {
		this();
		this.m_categary = categary;
	}
	
	public HAPStoryElementImp(String categary, String type, String id) {
		this(categary);
		this.setId(id);
		this.m_type = type;
	}
	
	@Override
	public String getCategary() {   return this.m_categary;    }

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
		Object categaryObj = jsonObj.opt(CATEGARY);
		if(categaryObj!=null)  this.m_categary = (String)categaryObj;
		this.m_type = jsonObj.getString(TYPE);
		this.m_entity = jsonObj.opt(ENTITY);
		this.m_status.buildObject(jsonObj.optJSONObject(STATUS), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CATEGARY, this.m_categary);
		jsonMap.put(TYPE, this.m_type);
		jsonMap.put(STATUS, HAPJsonUtility.buildJson(this.m_status, HAPSerializationFormat.JSON));
		jsonMap.put(ENTITY, HAPJsonUtility.buildJson(this.m_entity, HAPSerializationFormat.JSON));
	}
	
}
