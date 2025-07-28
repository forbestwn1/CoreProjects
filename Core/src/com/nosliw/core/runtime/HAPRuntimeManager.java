package com.nosliw.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPRuntimeManager {

	Map<HAPRuntimeInfo, HAPRuntimeEnvironment> m_runtimes = new LinkedHashMap<HAPRuntimeInfo, HAPRuntimeEnvironment>();
	
	public HAPRuntimeEnvironment getRuntime(HAPRuntimeInfo runtimeInfo){
		return this.m_runtimes.get(runtimeInfo);
	}

	
}
