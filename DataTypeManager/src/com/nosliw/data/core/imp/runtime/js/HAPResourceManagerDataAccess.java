package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;

public abstract class HAPResourceManagerDataAccess extends HAPResourceManagerImp{

	private HAPDataAccessRuntimeJS m_dataAccess = null;

	public HAPResourceManagerDataAccess(HAPDataAccessRuntimeJS dataAccess){
		this.m_dataAccess = dataAccess;
	}
	
	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		HAPJSResourceDependency dependency = this.m_dataAccess.getJSResourceDependency(resourceId);
		if(dependency!=null)  out = dependency.getDependency();
		return out;
	}

	protected HAPDataAccessRuntimeJS getDataAccess(){
		return this.m_dataAccess;
	}
}
