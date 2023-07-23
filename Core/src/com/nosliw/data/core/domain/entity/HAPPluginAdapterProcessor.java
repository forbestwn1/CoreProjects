package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPExecutable;

public abstract class HAPPluginAdapterProcessor {

	public abstract String getAdapterType();

	public abstract HAPExecutableEntity newExecutable();

	//process definition before value context
	public abstract void preProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext);

	//value context extension, variable resolve
	public void processValueContextExtension(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {}
	
	//matcher
	public void processValueContextDiscovery(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext){}
	
	//process definition after value context
	public void postProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext) {}

	
	
//	Object process(Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext);
	
}
