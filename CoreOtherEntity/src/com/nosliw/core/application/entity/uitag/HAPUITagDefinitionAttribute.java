package com.nosliw.core.application.entity.uitag;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.datadefinition.HAPParserDataDefinition;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

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
	
	public static HAPUITagDefinitionAttribute parseUITagDefinitionAttribute(JSONObject jsonObj, HAPManagerDataRule dataRuleManager) {
		String type = (String)jsonObj.opt(TYPE);
		if(type==null) {
			type = HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_SIMPLE;
		}
		switch(type) {
		case HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_SIMPLE:
		{
			HAPUITagDefinitionAttributeSimple simpleAttrDef = new HAPUITagDefinitionAttributeSimple();
			simpleAttrDef.buildObject(jsonObj, HAPSerializationFormat.JSON);
			return simpleAttrDef;
		}
		case HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_VARIABLE:
		{
			HAPUITagDefinitionAttributeVariable varAttrDef = new HAPUITagDefinitionAttributeVariable();
			varAttrDef.buildEntityInfoByJson(jsonObj);
			Object dfObj = jsonObj.opt(HAPUITagDefinitionAttributeVariable.DATADEFINITION);
			if(dfObj!=null) {
				varAttrDef.setDataDefinition(HAPParserDataDefinition.parseDataDefinitionWritable(dfObj, dataRuleManager));
			}
			String scope = (String)jsonObj.opt(HAPUITagDefinitionAttributeVariable.SCOPE);
			if(scope==null) {
				scope = HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PROTECTED;
			}
			varAttrDef.setScope(scope);
			return varAttrDef;
		}
		case HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_EVENT:
		{
			HAPUITagDefinitionAttributeEvent eventAttrDef = new HAPUITagDefinitionAttributeEvent();
			eventAttrDef.buildEntityInfoByJson(jsonObj);
			Object dfObj = jsonObj.opt(HAPUITagDefinitionAttributeEvent.EVENTDATADEFINITION);
			if(dfObj!=null) {
				eventAttrDef.setEventDataDefinition(HAPParserDataDefinition.parseDataDefinitionReadonly(dfObj));
			}
			return eventAttrDef;
		}
		}
		return null;
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
