package com.nosliw.core.resource;

import java.util.List;

import com.nosliw.core.runtime.HAPRuntimeInfo;

public interface HAPWithResourceDependency {

	void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo);
	
}
