package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute(baseName="RUNTIME")
public abstract class HAPRuntimeEnvironmentJS implements HAPRuntimeEnvironment{

	@HAPAttribute
	public static final String NODENAME_GATEWAY = "gatewayObj";
	
	@HAPAttribute
	public static final String GATEWAY_RESOURCE = "resources";
	
	
	private HAPResourceDiscovery m_resourceDiscovery;
	
	private HAPResourceManager m_resourceManager;
	
	private HAPExpressionManager m_expressionManager;
	
	private HAPGatewayManager m_gatewayManager;
	
	private HAPRuntime m_runtime;
	
	public HAPRuntimeEnvironmentJS(){}
	
	public HAPRuntimeEnvironmentJS(HAPResourceDiscovery resourceDiscovery, 
								   HAPResourceManager resourceMan, 
								   HAPExpressionManager expressionManager,
								    HAPGatewayManager gatewayManager,
								   HAPRuntime runtime){
		super();
		this.init(resourceDiscovery, resourceMan, expressionManager, gatewayManager, runtime);
	}
	
	protected void init(HAPResourceDiscovery resourceDiscovery,
					    HAPResourceManager resourceMan, 
					    HAPExpressionManager expressionManager,
					    HAPGatewayManager gatewayManager,
					    HAPRuntime runtime){
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
		this.m_expressionManager = expressionManager;
		this.m_gatewayManager = gatewayManager;
		this.m_runtime = runtime;
		
		this.getGatewayManager().registerGateway(GATEWAY_RESOURCE, new HAPGatewayRuntimeResource(this));
	}
	
	@Override
	public HAPResourceManager getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {		return this.m_resourceDiscovery;	}

	@Override
	public HAPExpressionManager getExpressionManager(){  return this.m_expressionManager;  }

	@Override
	public HAPGatewayManager getGatewayManager(){  return this.m_gatewayManager;   }
	
	@Override
	public HAPRuntime getRuntime() {		return this.m_runtime;	}

}
