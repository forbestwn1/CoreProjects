package com.nosliw.data.core.runtime.js.broswer;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPExecuteExpressionTask;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeImpJS;

public abstract class HAPRuntimeImpJSBroswer extends HAPRuntimeImpJS{

	private HAPResourceDiscovery m_resourceDiscovery;
	
	private HAPResourceManager m_resourceManager;

	public HAPRuntimeImpJSBroswer(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan){
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
	}

	
	@Override
	public HAPRuntimeInfo getRuntimeInfo() {		return new HAPRuntimeInfo(HAPConstant.RUNTIME_LANGUAGE_JS, HAPConstant.RUNTIME_ENVIRONMENT_BROWSER);	}

	@Override
	public HAPResourceManager getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {		return this.m_resourceDiscovery;	}

	
	@Override
	public void executeExpressionTask(HAPExecuteExpressionTask result) {
		// TODO Auto-generated method stub
		
	}
}
