package com.nosliw.core.xxx.runtimeenv;

/**
 * Runtime environment, 
 * Every runtime env is identified by two value: language and environment
 * Runtime provide components:
 * 		resource discovery
 * 		resource manager  
 * 		expression manager
 */
public interface HAPRuntimeEnvironment {

	public static final String id = System.currentTimeMillis()+"";

	/*
	HAPDataTypeManager getDataTypeManager();

	HAPManagerCodeTable getCodeTableManager();
	
	HAPParserDataExpression getDataExpressionParser();
	
	HAPManagerTask getTaskManager();
	
	HAPManagerActivity getActivityManager();
	
	HAPManagerProcess getProcessManager();

	HAPRuntimeProcess getProcessRuntime();

	HAPManagerResource getResourceManager();
	
	HAPGatewayManager getGatewayManager();

	HAPManagerService getServiceManager();
	
	HAPManagerResourceDefinition getResourceDefinitionManager();
	
	HAPManagerApplicationBrick getBrickManager();
	
	HAPManagerUITag getUITagManager();
	
	HAPManagerDynamicResource getDynamicResourceManager();
	
	HAPManagerCronJob getCronJobManager();
	
	HAPRuntime getRuntime();

	HAPDataTypeHelper getDataTypeHelper();
	*/
}
