package com.nosliw.data.core.runtime;

import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.service1.HAPManagerService;

/**
 * Runtime environment, 
 * Every runtime env is identified by two value: language and environment
 * Runtime provide components:
 * 		resource discovery
 * 		resource manager  
 * 		expression manager
 */
public interface HAPRuntimeEnvironment {

	HAPExpressionSuiteManager getExpressionSuiteManager();

	HAPManagerProcess getProcessManager();
	
	HAPResourceManagerRoot getResourceManager();

	HAPGatewayManager getGatewayManager();

	HAPManagerService getServiceManager();
	
	HAPRuntime getRuntime();
	
}
