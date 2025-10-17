package com.nosliw.core.application.entity.uitag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionWritable;

public class HAPUITagDefinitionAttributeVariable extends HAPUITagDefinitionAttribute{

	@HAPAttribute
	public static final String DATADEFINITION = "dataDefinition";

	private HAPDataDefinitionWritable m_dataDefinition;
	
	public HAPUITagDefinitionAttributeVariable() {
		super(HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_VARIABLE);
	}
	
	public HAPDataDefinitionWritable getDataDefinition() {	return this.m_dataDefinition;	}
	public void setDataDefinition(HAPDataDefinitionWritable dataDef) {   this.m_dataDefinition = dataDef;      }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
}
