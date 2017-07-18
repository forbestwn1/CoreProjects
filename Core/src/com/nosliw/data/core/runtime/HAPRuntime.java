package com.nosliw.data.core.runtime;

/**
 * Runtime environment, 
 * Every runtime env is identified by two value: language and environment
 * Runtime provide components:
 * 		executor
 * 		resource manager  
 */
public interface HAPRuntime {

	HAPRuntimeInfo getRuntimeInfo();
	
	void executeExpressionTask(HAPExecuteExpressionTask result);
	
	HAPResourceDiscovery getResourceDiscovery();
	
	HAPResourceManager getResourceManager();

	void start();
	
	void close();
}
