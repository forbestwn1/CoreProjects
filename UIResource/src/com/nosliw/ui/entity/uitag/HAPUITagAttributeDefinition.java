package com.nosliw.ui.entity.uitag;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public class HAPUITagAttributeDefinition extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String DEFAULTVALUE = "defaultValue";

	private Object m_defaultValue;
	
	public Object getDefaultValue() {    return this.m_defaultValue;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_defaultValue!=null)  jsonMap.put(DEFAULTVALUE, m_defaultValue.toString());
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_defaultValue = jsonObj.opt(DEFAULTVALUE);
		return true;  
	}
}
