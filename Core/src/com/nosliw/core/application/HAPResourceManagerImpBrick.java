package com.nosliw.core.application;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerImpBrick extends HAPResourceManagerImp{

	private HAPManagerApplicationBrick m_entityMan;
	
	public HAPResourceManagerImpBrick(HAPManagerApplicationBrick entityMan, HAPResourceManagerRoot rootResourceMan) {
		super(rootResourceMan);
		this.m_entityMan = entityMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPBundle bundle = this.m_entityMan.getBrickBundle((HAPResourceIdSimple)resourceId);
		return new HAPResource(resourceId, bundle.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}
}
