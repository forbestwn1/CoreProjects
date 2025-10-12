package com.nosliw.core.runtime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;

@Component
public class HAPRuntimeManager {

	public static final HAPRuntimeInfo RUNTIME_JS_RHION = new HAPRuntimeInfo(HAPConstantShared.RUNTIME_LANGUAGE_JS, HAPConstantShared.RUNTIME_ENVIRONMENT_RHINO);
	
	Map<HAPRuntimeInfo, HAPFactoryRuntime> m_runtimeFactorys = new LinkedHashMap<HAPRuntimeInfo, HAPFactoryRuntime>();
	
	Map<HAPRuntimeInfo, HAPRuntime> m_runtimes = new LinkedHashMap<HAPRuntimeInfo, HAPRuntime>();
	
//	public HAPRuntimeManager(List<HAPFactoryRuntime> runtimeFactorys) {
//		runtimeFactorys.stream().forEach(r->this.m_runtimeFactorys.put(r.getRuntimeInfo(), r));
//	}

	@Autowired
	public void setRuntimeFactorys(List<HAPFactoryRuntime> runtimeFactorys) {
		runtimeFactorys.stream().forEach(r->this.m_runtimeFactorys.put(r.getRuntimeInfo(), r));
	}
	
	public HAPRuntime getRuntime(HAPRuntimeInfo runtimeInfo){
		HAPRuntime out = this.m_runtimes.get(runtimeInfo);
		if(out==null) {
			out = this.m_runtimeFactorys.get(runtimeInfo).newRuntime();
			out.start();
			this.m_runtimes.put(runtimeInfo, out);
		}
		return out;
	}

	public HAPRuntime getDefaultRuntime() {
		HAPRuntimeInfo runtimeInfo = this.m_runtimeFactorys.keySet().iterator().next();
		return this.getRuntime(runtimeInfo);
	}

}
