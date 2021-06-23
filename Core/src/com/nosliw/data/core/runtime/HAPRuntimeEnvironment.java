package com.nosliw.data.core.runtime;

import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.script.expression.HAPManagerScript;
import com.nosliw.data.core.service.definition.HAPManagerService;
import com.nosliw.data.core.story.HAPManagerStory;

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
	
	HAPManagerExpression getExpressionManager();

	HAPManagerScript getScriptManager();

	HAPManagerActivity getActivityManager();
	
	HAPManagerProcess getProcessManager();

	HAPRuntimeProcess getProcessRuntime();

	HAPResourceManagerRoot getResourceManager();

	HAPGatewayManager getGatewayManager();

	HAPManagerService getServiceManager();
	
	HAPManagerResourceDefinition getResourceDefinitionManager();
	
	HAPManagerDynamicResource getDynamicResourceManager();
	
	HAPManagerCronJob getCronJobManager();
	
	HAPManagerStory getStoryManager();
	
	HAPRuntime getRuntime();

	HAPDataTypeHelper getDataTypeHelper();
}
