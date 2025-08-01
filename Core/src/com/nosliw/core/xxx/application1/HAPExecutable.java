package com.nosliw.core.xxx.application1;

import java.util.List;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceData;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.runtime.HAPRuntimeInfo;

//as an executable, it related with resource data
public interface HAPExecutable extends HAPSerializable{

	HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo);
	
	//resource dependency 
	List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager);

	
}
