package com.nosliw.data.core.runtime.js;

import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPRuntimeEnvironmentJS implements HAPRuntimeEnvironment{

	private HAPResourceDiscovery m_resourceDiscovery;
	
	private HAPResourceManager m_resourceManager;
	
	private HAPExpressionManager m_expressionManager;
	
	private HAPRuntime m_runtime;
	
	public HAPRuntimeEnvironmentJS(){}
	
	public HAPRuntimeEnvironmentJS(HAPResourceDiscovery resourceDiscovery, 
								   HAPResourceManager resourceMan, 
								   HAPExpressionManager expressionManager,
								   HAPRuntime runtime){
		super();
		this.init(resourceDiscovery, resourceMan, expressionManager, runtime);
	}
	
	protected void init(HAPResourceDiscovery resourceDiscovery,
					    HAPResourceManager resourceMan, 
					    HAPExpressionManager expressionManager,
					    HAPRuntime runtime){
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
		this.m_expressionManager = expressionManager;
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPResourceManager getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {		return this.m_resourceDiscovery;	}

	@Override
	public HAPExpressionManager getExpressionManager(){  return this.m_expressionManager;  }

	@Override
	public HAPRuntime getRuntime() {		return this.m_runtime;	}

}
