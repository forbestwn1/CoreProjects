package com.nosliw.data.core.service.resource;

import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.core.application.service.HAPManagerServiceInterface;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerServiceInterface  extends HAPResourceManagerImp{

	private HAPManagerServiceInterface m_serviceInterfaceMan;
	
	public HAPResourceManagerServiceInterface(HAPManagerServiceInterface serviceInterfaceMan, HAPManagerResource rootResourceMan){
		super(rootResourceMan);
		this.m_serviceInterfaceMan = serviceInterfaceMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPBlockServiceInterface serviceInterfaceInfo =  this.m_serviceInterfaceMan.getServiceInterface(new HAPResourceIdServiceInterface((HAPResourceIdSimple)resourceId).getServiceInterfaceId());
		HAPResourceDataServiceInterface resourceData = new HAPResourceDataServiceInterface(serviceInterfaceInfo);
		return new HAPResource(resourceId, resourceData, HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

}
