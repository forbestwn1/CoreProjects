package com.nosliw.core.runtime;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.runtime.execute.HAPFactoryExecutorRuntime;
import com.nosliw.core.runtime.execute.HAPExecutorRuntime;

@Component
public class HAPRuntimeManager {

	public static final HAPRuntimeInfo RUNTIME_JS_RHION = new HAPRuntimeInfo(HAPConstantShared.RUNTIME_LANGUAGE_JS, HAPConstantShared.RUNTIME_ENVIRONMENT_RHINO);
	
	Map<HAPRuntimeInfo, HAPFactoryExecutorRuntime> m_runtimeExecutorFactorys = new LinkedHashMap<HAPRuntimeInfo, HAPFactoryExecutorRuntime>();
	
	Map<HAPRuntimeInfo, HAPExecutorRuntime> m_runtimeExecutors = new LinkedHashMap<HAPRuntimeInfo, HAPExecutorRuntime>();
	
//	public HAPRuntimeManager(List<HAPFactoryExecutorRuntime> runtimeFactorys) {
//		runtimeFactorys.stream().forEach(r->this.m_runtimeFactorys.put(r.getRuntimeInfo(), r));
//	}

	@Autowired
	public void setRuntimeFactorys(List<HAPFactoryExecutorRuntime> runtimeFactorys) {
		runtimeFactorys.stream().forEach(r->this.m_runtimeExecutorFactorys.put(r.getRuntimeInfo(), r));
	}
	
	public HAPExecutorRuntime getRuntimeExecutor(HAPRuntimeInfo runtimeInfo){
		HAPExecutorRuntime out = this.m_runtimeExecutors.get(runtimeInfo);
		if(out==null) {
			out = this.m_runtimeExecutorFactorys.get(runtimeInfo).newRuntimeExecutor();
			out.start();
			this.m_runtimeExecutors.put(runtimeInfo, out);
		}
		return out;
	}

	public HAPExecutorRuntime getDefaultRuntimeExecutor() {
		HAPRuntimeInfo runtimeInfo = this.m_runtimeExecutorFactorys.keySet().iterator().next();
		return this.getRuntimeExecutor(runtimeInfo);
	}

}
