package com.nosliw.core.runtime;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;

@Component
public class HAPRuntimeManager {

	public static final HAPRuntimeInfo RUNTIME_JS_RHION = new HAPRuntimeInfo(HAPConstantShared.RUNTIME_LANGUAGE_JS, HAPConstantShared.RUNTIME_ENVIRONMENT_RHINO);
	
	@Autowired
	Map<HAPRuntimeInfo, HAPFactoryRuntime> m_runtimeFactorys;
	
	public HAPRuntimeManager(List<HAPFactoryRuntime> runtimeFactorys) {
		runtimeFactorys.stream().forEach(r->this.m_runtimeFactorys.put(r.getRuntimeInfo(), r));
	}
	
	public HAPRuntime getRuntime(HAPRuntimeInfo runtimeInfo){
		return this.m_runtimeFactorys.get(runtimeInfo).newRuntime();
	}

	public HAPRuntime getDefaultRuntime() {
		return this.m_runtimeFactorys.values().iterator().next().newRuntime();
	}

}
