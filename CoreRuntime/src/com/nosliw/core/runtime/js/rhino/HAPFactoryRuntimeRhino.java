package com.nosliw.core.runtime.js.rhino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.core.gateway.HAPGatewayManager;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.runtime.HAPRuntimeManager;
import com.nosliw.core.runtime.execute.HAPFactoryExecutorRuntime;
import com.nosliw.core.runtime.execute.HAPExecutorRuntime;

@Component
public class HAPFactoryRuntimeRhino implements HAPFactoryExecutorRuntime{

	@Autowired
	private HAPGatewayManager m_gatewayManager;

	@Override
	public HAPRuntimeInfo getRuntimeInfo() {  return HAPRuntimeManager.RUNTIME_JS_RHION;  }

	@Override
	public HAPExecutorRuntime newRuntimeExecutor() {
		return new HAPExecutorRuntimeImpRhino(this.m_gatewayManager);
	}

}
