package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayCriteriaOperation;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResource;

@HAPEntityWithAttribute(baseName="RUNTIME")
public abstract class HAPRuntimeEnvironmentJS implements HAPRuntimeEnvironment{

	@HAPAttribute
	public static final String NODENAME_GATEWAY = "gatewayObj";
	
	@HAPAttribute
	public static final String GATEWAY_RESOURCE = "resources";
	
	@HAPAttribute
	public static final String GATEWAY_CRITERIA = "criteria";
	
	private HAPResourceManagerRoot m_resourceManager;
	
	private HAPExpressionManager m_expressionManager;
	
	private HAPGatewayManager m_gatewayManager;
	
	private HAPRuntime m_runtime;
	
	public HAPRuntimeEnvironmentJS(){}
	
	public HAPRuntimeEnvironmentJS(HAPResourceManagerRoot resourceMan, 
								   HAPExpressionManager expressionManager,
								    HAPGatewayManager gatewayManager,
								   HAPRuntime runtime){
		super();
		this.init(resourceMan, expressionManager, gatewayManager, runtime);
	}
	
	protected void init(HAPResourceManagerRoot resourceMan, 
					    HAPExpressionManager expressionManager,
					    HAPGatewayManager gatewayManager,
					    HAPRuntime runtime){
		this.m_resourceManager = resourceMan;
		this.m_expressionManager = expressionManager;

		//gateway
		this.m_gatewayManager = gatewayManager;
		this.getGatewayManager().registerGateway(GATEWAY_RESOURCE, new HAPGatewayResource(this));
		this.getGatewayManager().registerGateway(GATEWAY_CRITERIA, new HAPGatewayCriteriaOperation());

		//runtime
		this.m_runtime = runtime;
		this.m_runtime.start();
	}
	
	@Override
	public HAPResourceManagerRoot getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPExpressionManager getExpressionManager(){  return this.m_expressionManager;  }

	@Override
	public HAPGatewayManager getGatewayManager(){  return this.m_gatewayManager;   }
	
	@Override
	public HAPRuntime getRuntime() {		return this.m_runtime;	}

}
