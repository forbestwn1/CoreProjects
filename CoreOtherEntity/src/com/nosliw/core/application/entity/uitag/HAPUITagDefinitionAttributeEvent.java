package com.nosliw.core.application.entity.uitag;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionReadonly;

public class HAPUITagDefinitionAttributeEvent extends HAPUITagDefinitionAttribute{

	@HAPAttribute
	public static final String EVENTDATADEFINITION = "eventDataDefinition";

	private HAPDataDefinitionReadonly m_eventDataDefinition;
	
	public HAPUITagDefinitionAttributeEvent() {
		super(HAPConstantShared.UITAGDEFINITION_ATTRIBUTETYPE_EVENT);
	}
	
	public HAPDataDefinitionReadonly getEventDataDefinition() {	return this.m_eventDataDefinition;	}
	public void setEventDataDefinition(HAPDataDefinitionReadonly eventDataDef) {   this.m_eventDataDefinition = eventDataDef;      }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTDATADEFINITION, this.m_eventDataDefinition.toStringValue(HAPSerializationFormat.JSON));
	}
	
}
