package com.nosliw.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HAPRuntimeManager {

	@Autowired
	Map<String, HAPRuntime> m_runtimes = new LinkedHashMap<String, HAPRuntime>();
	
	public HAPRuntime getRuntime(HAPRuntimeInfo runtimeInfo){
		return this.m_runtimes.get(runtimeInfo.toString());
	}

}
