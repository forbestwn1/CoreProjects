package com.nosliw.core.application.entity.uitag;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionWritable;

public class HAPUITagDefinitionAttributeVariable extends HAPUITagDefinitionAttribute{

	@HAPAttribute
	public static final String DATADEFINITION = "dataDefinition";

	private HAPDataDefinitionWritable m_dataDefinition;
	
	public HAPUITagDefinitionAttributeVariable() {
		super(HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_VARIABLE);
	}
	
	public HAPDataDefinitionWritable getDataDefinition() {
		return this.m_dataDefinition;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		JSONObject dfObj = jsonObj.optJSONObject(DATADEFINITION);
		if(dfObj!=null) {
			this.m_dataDefinition = new HAPDataDefinitionWritable();
			this.m_dataDefinition.buildObject(dfObj, HAPSerializationFormat.JSON);
		}
		return true;  
	}
}
