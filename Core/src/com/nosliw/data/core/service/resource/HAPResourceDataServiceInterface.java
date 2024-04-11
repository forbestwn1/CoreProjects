package com.nosliw.data.core.service.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPResourceDataServiceInterface extends HAPResourceDataJSValueImp{

	private HAPBrickServiceInterface m_serviceInterfaceInfo;
	
	public HAPResourceDataServiceInterface(HAPBrickServiceInterface serviceInterfaceInfo){
		this.m_serviceInterfaceInfo = serviceInterfaceInfo;
	}
	
	public HAPBrickServiceInterface getServiceInterfaceInfo(){ return this.m_serviceInterfaceInfo;  }
	
	@Override
	public String getValue() {
		return this.m_serviceInterfaceInfo.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
