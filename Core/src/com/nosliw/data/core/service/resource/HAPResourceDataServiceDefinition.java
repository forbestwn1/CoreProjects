package com.nosliw.data.core.service.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.service.profile.HAPBrickServiceProfile;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPResourceDataServiceDefinition extends HAPResourceDataJSValueImp{

	private HAPBrickServiceProfile m_serviceDefinition;
	
	public HAPResourceDataServiceDefinition(HAPBrickServiceProfile serviceDefinition){
		this.m_serviceDefinition = serviceDefinition;
	}
	
	public HAPBrickServiceProfile getServiceDefinition(){ return this.m_serviceDefinition;  }
	
	@Override
	public String getValue() {
		return this.m_serviceDefinition.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
