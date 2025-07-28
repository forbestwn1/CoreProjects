package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPContextReference extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";

	@HAPAttribute
	public static final String CATEGARY = "categary";

	//name refered to attachment
	private String m_name;
	
	//for group context only. categary to put context to group context
	private String m_categary;
	
	public HAPContextReference() {
		
	}
	
	public String getName() {   return this.m_name;  }
	
	public String getCategary() {    return this.m_categary;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(CATEGARY, this.m_categary);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.getString(NAME);
		this.m_categary = (String)jsonObj.opt(CATEGARY);
		return true;
	}

}
