package com.nosliw.core.application;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceData;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

//as an executable, it related with resource data
public interface HAPExecutable extends HAPSerializable{

	HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo);
	
	//resource dependency 
	List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager);

	
}
