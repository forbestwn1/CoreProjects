package com.nosliw.data.core.service.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPResourceDataServiceDefinition extends HAPResourceDataJSValueImp{

	private HAPBlockServiceProfile m_serviceDefinition;
	
	public HAPResourceDataServiceDefinition(HAPBlockServiceProfile serviceDefinition){
		this.m_serviceDefinition = serviceDefinition;
	}
	
	public HAPBlockServiceProfile getServiceDefinition(){ return this.m_serviceDefinition;  }
	
	@Override
	public String getValue() {
		return this.m_serviceDefinition.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
