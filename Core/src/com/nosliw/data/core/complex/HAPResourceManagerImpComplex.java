package com.nosliw.data.core.complex;

import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerImpComplex extends HAPResourceManagerImp{

	private HAPManagerComplexEntity m_complexEntityMan;
	
	public HAPResourceManagerImpComplex(HAPManagerComplexEntity complexEntityMan, HAPResourceManagerRoot rootResourceMan) {
		super(rootResourceMan);
		this.m_complexEntityMan = complexEntityMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPExecutableBundle bundle = this.m_complexEntityMan.getComplexEntityResourceBundle((HAPResourceIdSimple)resourceId);
		return new HAPResource(resourceId, bundle.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}
}
