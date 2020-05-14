package com.nosliw.data.core.runtime;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.script.expression.HAPManagerScript;
import com.nosliw.data.core.service.provide.HAPManagerService;
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
	
	HAPManagerExpression getExpressionManager();

	HAPManagerScript getScriptManager();

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
	
}
