package com.nosliw.data.core.service.resource;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.service.interfacee.HAManagerServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPInfoServiceInterface;

public class HAPResourceManagerServiceInterface  extends HAPResourceManagerImp{

	private HAManagerServiceInterface m_serviceInterfaceMan;
	
	public HAPResourceManagerServiceInterface(HAManagerServiceInterface serviceInterfaceMan, HAPResourceManagerRoot rootResourceMan){
		super(rootResourceMan);
		this.m_serviceInterfaceMan = serviceInterfaceMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPInfoServiceInterface serviceInterfaceInfo =  this.m_serviceInterfaceMan.getServiceInterface(new HAPResourceIdServiceInterface((HAPResourceIdSimple)resourceId).getServiceInterfaceId());
		HAPResourceDataServiceInterface resourceData = new HAPResourceDataServiceInterface(serviceInterfaceInfo);
		return new HAPResource(resourceId, resourceData, HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

}
