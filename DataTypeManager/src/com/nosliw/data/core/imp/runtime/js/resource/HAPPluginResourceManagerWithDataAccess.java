package com.nosliw.data.core.imp.runtime.js.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPJSResourceDependency;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPPluginResourceManagerWithDataAccess implements HAPPluginResourceManager{

	private HAPDataAccessRuntimeJS m_dataAccess = null;

	public HAPPluginResourceManagerWithDataAccess(HAPDataAccessRuntimeJS dataAccess) {
		this.m_dataAccess = dataAccess;
	}
	
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		HAPJSResourceDependency dependency = this.m_dataAccess.getJSResourceDependency((HAPResourceIdSimple)resourceId);
		if(dependency!=null) {
			out = dependency.getDependency();
		}
		return out;
	}

	protected HAPDataAccessRuntimeJS getDataAccess(){
		return this.m_dataAccess;
	}

}
