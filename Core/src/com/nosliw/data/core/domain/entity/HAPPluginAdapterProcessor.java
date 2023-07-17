package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPExecutable;

public abstract class HAPPluginAdapterProcessor {

	public abstract String getAdapterType();

	//process definition before value context
	public abstract HAPExecutable preProcess(Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext);

	//value context extension, variable resolve
	public void processValueContextExtension(HAPExecutable adapterExe, Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext) {}
	
	//matcher
	public void processValueContextDiscovery(HAPExecutable adapterExe, Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext){}
	
	//process definition after value context
	public void postProcess(HAPExecutable adapterExe, Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext) {}

	
	
//	Object process(Object adapter, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutable parentEntityExecutable, HAPContextProcessor parentContext);
	
}
