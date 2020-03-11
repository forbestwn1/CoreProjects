package com.nosliw.data.core.runtime;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;

//as an executable, it related with resource data
public interface HAPExecutable extends HAPSerializable{

	HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo);
	
	//resource dependency 
	List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo);

	
}
