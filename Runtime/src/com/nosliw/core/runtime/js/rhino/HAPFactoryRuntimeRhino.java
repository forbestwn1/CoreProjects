package com.nosliw.core.runtime.js.rhino;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.core.gateway.HAPGatewayManager;
import com.nosliw.core.runtime.HAPFactoryRuntime;
import com.nosliw.core.runtime.HAPRuntime;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.runtime.HAPRuntimeManager;

@Component
public class HAPFactoryRuntimeRhino implements HAPFactoryRuntime{

	@Autowired
	private HAPGatewayManager m_gatewayManager;

	@Override
	public HAPRuntimeInfo getRuntimeInfo() {  return HAPRuntimeManager.RUNTIME_JS_RHION;  }

	@Override
	public HAPRuntime newRuntime() {
		return new HAPRuntimeImpRhino(this.m_gatewayManager);
	}

}
