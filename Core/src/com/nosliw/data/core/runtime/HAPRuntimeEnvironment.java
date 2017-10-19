package com.nosliw.data.core.runtime;

import com.nosliw.data.core.expression.HAPExpressionManager;

/**
 * Runtime environment, 
 * Every runtime env is identified by two value: language and environment
 * Runtime provide components:
 * 		resource discovery
 * 		resource manager  
 * 		expression manager
 */
public interface HAPRuntimeEnvironment {

	HAPExpressionManager getExpressionManager();
	
//	HAPResourceDiscovery getResourceDiscovery();
	
	HAPResourceManagerRoot getResourceManager();

	HAPGatewayManager getGatewayManager();

	HAPRuntime getRuntime();
	
}
