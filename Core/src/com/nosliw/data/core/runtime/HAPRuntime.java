package com.nosliw.data.core.runtime;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

/**
 * Runtime environment, 
 * Every runtime env is identified by two value: language and environment
 * Runtime provide components:
 * 		executor
 * 		resource manager  
 */
public interface HAPRuntime {

	HAPRuntimeInfo getRuntimeInfo();
	
	HAPData executeExpression(HAPExpression expression);
	
	HAPResourceDiscovery getResourceDiscovery();
	
	HAPResourceManager getResourceManager();
	
}
