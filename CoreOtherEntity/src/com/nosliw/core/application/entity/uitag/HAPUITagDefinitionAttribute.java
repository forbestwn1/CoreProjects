package com.nosliw.core.application.entity.uitag;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public abstract class HAPUITagDefinitionAttribute extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String TYPE = "type";

	@HAPAttribute
	public static final String DEFAULTVALUE = "defaultValue";

	private Object m_defaultValue;
	
	private String m_type; 
	
	public HAPUITagDefinitionAttribute(String type) {
		this.m_type = type;
	}
	
	public String getType() {
		return this.m_type;
	}
	
	public Object getDefaultValue() {    return this.m_defaultValue;     }
	
	public static HAPUITagDefinitionAttribute parseUITagDefinitionAttribute(JSONObject jsonObj) {
		HAPUITagDefinitionAttribute out = null;
		String type = (String)jsonObj.opt(TYPE);
		if(type==null) {
			type = HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_SIMPLE;
		}
		switch(type) {
		case HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_SIMPLE:
			out = new HAPUITagDefinitionAttributeSimple();
			break;
		case HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_VARIABLE:
			out = new HAPUITagDefinitionAttributeVariable();
			break;
		}
		if(out!=null) {
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		if(this.m_defaultValue!=null) {
			jsonMap.put(DEFAULTVALUE, m_defaultValue.toString());
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_defaultValue = jsonObj.opt(DEFAULTVALUE);
		return true;  
	}
}
