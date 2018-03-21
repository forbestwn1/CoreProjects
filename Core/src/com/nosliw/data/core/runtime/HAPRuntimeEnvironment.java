package com.nosliw.data.core.runtime;

import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.task.HAPManagerTask;

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

	HAPManagerTask getTaskManager();
	
	HAPResourceManagerRoot getResourceManager();

	HAPGatewayManager getGatewayManager();

	HAPDataSourceManager getDataSourceManager();
	
	HAPRuntime getRuntime();
	
}
