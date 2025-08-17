package com.nosliw.core.application.common.datadefinition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionResult extends HAPEntityInfoImp{

	@HAPAttribute
	public static String DATADEFINITION = "dataDefinition";

	private HAPDataDefinitionReadonly m_dataDefinition;
	
	public HAPDefinitionResult() {
		this.m_dataDefinition = new HAPDataDefinitionReadonly();
	}
	
	public HAPDataDefinitionReadonly getDataDefinition() {		return this.m_dataDefinition;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DATADEFINITION, this.m_dataDefinition.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		super.buildObject(value, format);
		JSONObject jsonValue = (JSONObject)value;
		
		Object dataDefObj = jsonValue.opt(DATADEFINITION);
		if(dataDefObj!=null) {
			this.m_dataDefinition.buildObject(dataDefObj, HAPSerializationFormat.JSON);
		}
		return true;
	}
}
