package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceDiscoveryJSImp;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSConverter;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSHelper;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSLibrary;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerJSOperation;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpJSRhino;

public class HAPRuntimeImpJSRhinoImp extends HAPRuntimeImpJSRhino{

	HAPModuleRuntimeJS m_runtimeJSModule;
	
	public HAPRuntimeImpJSRhinoImp(HAPModuleRuntimeJS runtimeJSModule) {
		super(new HAPResourceDiscoveryJSImp(runtimeJSModule.getRuntimeJSDataAccess()), new HAPResourceManagerJS());

		this.m_runtimeJSModule = runtimeJSModule;
		
		HAPResourceManagerJS resourceMan = (HAPResourceManagerJS)this.getResourceManager();
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION, new HAPResourceManagerJSOperation(m_runtimeJSModule.getRuntimeJSDataAccess(), m_runtimeJSModule.getDataTypeDataAccess()));
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, new HAPResourceManagerJSConverter(m_runtimeJSModule.getRuntimeJSDataAccess()));
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary());
		resourceMan.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, new HAPResourceManagerJSHelper(m_runtimeJSModule.getRuntimeJSDataAccess()));
	}
}
