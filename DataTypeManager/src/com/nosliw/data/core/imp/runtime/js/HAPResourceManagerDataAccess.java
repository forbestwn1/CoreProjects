package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPResourceManagerDataAccess extends HAPResourceManagerImp{

	private HAPDataAccessRuntimeJS m_dataAccess = null;

	public HAPResourceManagerDataAccess(HAPDataAccessRuntimeJS dataAccess, HAPManagerResource rootResourceMan){
		super(rootResourceMan);
		this.m_dataAccess = dataAccess;
	}
	
	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		HAPJSResourceDependency dependency = this.m_dataAccess.getJSResourceDependency((HAPResourceIdSimple)resourceId);
		if(dependency!=null)  out = dependency.getDependency();
		return out;
	}

	protected HAPDataAccessRuntimeJS getDataAccess(){
		return this.m_dataAccess;
	}
}
