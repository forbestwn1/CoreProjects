package com.nosliw.data.core.service.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;
import com.nosliw.data.core.service.definition.HAPDefinitionService;

public class HAPResourceDataServiceDefinition extends HAPResourceDataJSValueImp{

	private HAPDefinitionService m_serviceDefinition;
	
	public HAPResourceDataServiceDefinition(HAPDefinitionService serviceDefinition){
		this.m_serviceDefinition = serviceDefinition;
	}
	
	public HAPDefinitionService getServiceDefinition(){ return this.m_serviceDefinition;  }
	
	@Override
	public String getValue() {
		return this.m_serviceDefinition.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
