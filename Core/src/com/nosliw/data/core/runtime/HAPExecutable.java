package com.nosliw.data.core.runtime;

import java.util.List;

//as an executable, it related with resource data
public interface HAPExecutable {

	HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo);
	
	//resource dependency 
	List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo);

	
}
