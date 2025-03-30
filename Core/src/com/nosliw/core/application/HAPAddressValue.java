package com.nosliw.core.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPAddressValue extends HAPSerializableImp{

	@HAPAttribute
	public static final String CATEGARY = "categary";

	@HAPAttribute
	public static final String ID = "id";

	private String m_categary;
	
	private String m_id;
	
	public String getCategary() {    return this.m_categary;    }
	
	public String getId() {    return this.m_id;       }
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		JSONObject jsonObj = (JSONObject)json;
		this.m_categary = (String)jsonObj.opt(CATEGARY);
		this.m_id = jsonObj.getString(ID);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(CATEGARY, this.m_categary);
		jsonMap.put(ID, m_id);
	}
	
}
