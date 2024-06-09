package com.nosliw.data.core.runtime;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.service.HAPManagerService;
import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.domain.definition.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPManagerDomainEntityExecutable;
import com.nosliw.data.core.domain.entity.expression.data1.HAPParserDataExpression;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPRuntimeProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.script.expression1.HAPManagerScript;
import com.nosliw.data.core.story.HAPManagerStory;
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
	
	HAPManagerScript getScriptManager();

	HAPManagerTask getTaskManager();
	
	HAPManagerActivity getActivityManager();
	
	HAPManagerProcess getProcessManager();

	HAPRuntimeProcess getProcessRuntime();

	HAPResourceManager getResourceManager();
	
	HAPGatewayManager getGatewayManager();

	HAPManagerService getServiceManager();
	
	HAPManagerResourceDefinition getResourceDefinitionManager();
	
	HAPManagerDomainEntityDefinition getDomainEntityDefinitionManager();
	
	HAPManagerDomainEntityExecutable getDomainEntityExecutableManager();
	
	HAPManagerApplicationBrick getBrickManager();
	
	HAPManagerDynamicResource getDynamicResourceManager();
	
	HAPManagerCronJob getCronJobManager();
	
	HAPManagerStory getStoryManager();
	
	HAPRuntime getRuntime();

	HAPDataTypeHelper getDataTypeHelper();
}
