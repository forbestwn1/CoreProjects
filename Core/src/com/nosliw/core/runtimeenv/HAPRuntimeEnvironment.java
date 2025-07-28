package com.nosliw.core.runtimeenv;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.service.HAPManagerService;
import com.nosliw.core.application.uitag.HAPManagerUITag;
import com.nosliw.core.gateway.HAPGatewayManager;
import com.nosliw.core.runtime.HAPRuntime;
import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPRuntimeProcess;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
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

	public static final String id = System.currentTimeMillis()+"";
	
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
}
