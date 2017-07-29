package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSImp;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpJSRhino;

public class HAPRuntimeImpJSRhinoImp extends HAPRuntimeImpJSRhino{

	HAPModuleRuntimeJS m_runtimeJSModule;
	
	public HAPRuntimeImpJSRhinoImp(HAPModuleRuntimeJS runtimeJSModule) {
		super(new HAPResourceDiscoveryJSImp(runtimeJSModule.getRuntimeJSDataAccess()), new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess()));

		this.m_runtimeJSModule = runtimeJSModule;
	}
}
