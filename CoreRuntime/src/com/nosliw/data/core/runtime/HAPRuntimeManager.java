package com.nosliw.data.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPRuntimeManager {

	Map<HAPRuntimeInfo, HAPRuntimeEnvironment> m_runtimes = new LinkedHashMap<HAPRuntimeInfo, HAPRuntimeEnvironment>();
	
	public HAPRuntimeEnvironment getRuntime(HAPRuntimeInfo runtimeInfo){
		return this.m_runtimes.get(runtimeInfo);
	}

	
}
