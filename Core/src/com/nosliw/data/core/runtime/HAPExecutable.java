package com.nosliw.data.core.runtime;

import java.util.List;

import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependent;

//as an executable, it related with resource data
public interface HAPExecutable {

	HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo);
	
	//resource dependency 
	List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo);

	
}
