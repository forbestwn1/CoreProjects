package com.nosliw.data.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPRuntimeManager {

	Map<HAPRuntimeInfo, HAPRuntime> m_runtimes = new LinkedHashMap<HAPRuntimeInfo, HAPRuntime>();
	
	public HAPRuntime getRuntime(HAPRuntimeInfo runtimeInfo){
		return this.m_runtimes.get(runtimeInfo);
	}

	public void registerRuntime(HAPRuntime runtime){
		this.m_runtimes.put(runtime.getRuntimeInfo(), runtime);
	}
	
}
