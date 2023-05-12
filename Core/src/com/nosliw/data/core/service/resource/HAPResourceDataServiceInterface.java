package com.nosliw.data.core.service.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.service.interfacee.HAPInfoServiceInterface;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPResourceDataServiceInterface extends HAPResourceDataJSValueImp{

	private HAPInfoServiceInterface m_serviceInterfaceInfo;
	
	public HAPResourceDataServiceInterface(HAPInfoServiceInterface serviceInterfaceInfo){
		this.m_serviceInterfaceInfo = serviceInterfaceInfo;
	}
	
	public HAPInfoServiceInterface getServiceInterfaceInfo(){ return this.m_serviceInterfaceInfo;  }
	
	@Override
	public String getValue() {
		return this.m_serviceInterfaceInfo.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
