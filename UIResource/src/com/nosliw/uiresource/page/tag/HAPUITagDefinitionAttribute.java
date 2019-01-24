package com.nosliw.uiresource.page.tag;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;


public class HAPUITagDefinitionAttribute extends HAPSerializableImp{

	@HAPAttribute
	public static final String NAME = "name";
	@HAPAttribute
	public static final String DESCRIPTION = "description";
	
	private String m_name;

	private String m_description;
	
	private HAPDataTypeCriteria m_criteria;

	public String getName() {   return this.m_name;  }
	
	public String getDescription() {  return this.m_description;  }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(DESCRIPTION, this.m_description);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		this.m_description = jsonObj.optString(DESCRIPTION);
		return true;  
	}
}
