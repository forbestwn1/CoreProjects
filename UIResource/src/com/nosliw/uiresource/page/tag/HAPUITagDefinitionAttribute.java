package com.nosliw.uiresource.page.tag;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public class HAPUITagDefinitionAttribute extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String DEFAULTVALUE = "defaultValue";

	private String m_defaultValue;
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_defaultValue!=null)  jsonMap.put(DEFAULTVALUE, m_defaultValue);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_defaultValue = (String)jsonObj.opt(DEFAULTVALUE);
		return true;  
	}
}
