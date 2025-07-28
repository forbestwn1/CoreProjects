package com.nosliw.data.core.service.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPResourceDataServiceInterface extends HAPResourceDataJSValueImp{

	private HAPBlockServiceInterface m_serviceInterfaceInfo;
	
	public HAPResourceDataServiceInterface(HAPBlockServiceInterface serviceInterfaceInfo){
		this.m_serviceInterfaceInfo = serviceInterfaceInfo;
	}
	
	public HAPBlockServiceInterface getServiceInterfaceInfo(){ return this.m_serviceInterfaceInfo;  }
	
	@Override
	public String getValue() {
		return this.m_serviceInterfaceInfo.toStringValue(HAPSerializationFormat.JSON_FULL);
	}

}
