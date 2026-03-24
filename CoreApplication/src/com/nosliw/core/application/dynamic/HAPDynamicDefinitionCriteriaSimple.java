package com.nosliw.core.application.dynamic;

import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;

public class HAPDynamicDefinitionCriteriaSimple extends HAPDynamicDefinitionCriteria{

	public final static String DEFINITION = "definition"; 

	private HAPCriteriaBrick m_criteriaDefinition;

	public HAPDynamicDefinitionCriteriaSimple() {
		super(HAPConstantShared.DYNAMICDEFINITION_CRITERIA_SIMPLE);
	}

	public HAPCriteriaBrick getCriteriaDefinition() {	return this.m_criteriaDefinition;	}
	
	public void setCriteriaDefinition(HAPCriteriaBrick brickCriteria) {    this.m_criteriaDefinition = brickCriteria;       }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DEFINITION, HAPManagerSerialize.getInstance().toStringValue(m_criteriaDefinition, HAPSerializationFormat.JSON));
	}
}
