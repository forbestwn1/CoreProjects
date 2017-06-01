package com.nosliw.data.core.runtime;

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
	
	void executeExpression(HAPExpression expression, HAPExpressionResult result);
	
	HAPResourceDiscovery getResourceDiscovery();
	
	HAPResourceManager getResourceManager();

	void start();
	
	void close();
}
