package com.nosliw.data.core.imp.runtime.js.browser;

import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSImp;
import com.nosliw.data.core.runtime.js.broswer.HAPRuntimeImpJSBroswer;

public class HAPRuntimeImpJSBroswerImp extends HAPRuntimeImpJSBroswer{

	HAPModuleRuntimeJS m_runtimeJSModule;
	
	public HAPRuntimeImpJSBroswerImp(HAPModuleRuntimeJS runtimeJSModule) {
		super(new HAPResourceDiscoveryJSImp(runtimeJSModule.getRuntimeJSDataAccess()), new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess()));

		this.m_runtimeJSModule = runtimeJSModule;
	}

}
