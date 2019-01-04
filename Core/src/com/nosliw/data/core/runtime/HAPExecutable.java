package com.nosliw.data.core.runtime;

import java.util.List;

public interface HAPExecutable {

	HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo);
	
	//resource dependency 
	List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo);

	
}
