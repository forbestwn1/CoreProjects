package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.runtime.HAPExecutable;

public interface HAPPluginAdapterProcessor {

	public abstract String getAdapterType();

	public abstract HAPExecutableEntity newExecutable();

	//process definition before value context
	void preProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext);
	
	//matcher
	void processValueContextDiscovery(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext);
	
	//process definition after value context
	void postProcess(HAPExecutableEntity adapterExe, HAPExecutable childEntityExecutable, HAPContextProcessor childContext, HAPExecutableEntity parentEntityExecutable, HAPContextProcessor parentContext);

}
