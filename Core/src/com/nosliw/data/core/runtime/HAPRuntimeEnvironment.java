package com.nosliw.data.core.runtime;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.core.template.HAPManagerTemplate;

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

	HAPRuntimeProcess getProcessRuntime();

	HAPResourceManagerRoot getResourceManager();

	HAPGatewayManager getGatewayManager();

	HAPManagerService getServiceManager();
	
	HAPManagerResourceDefinition getResourceDefinitionManager();
	
	HAPManagerTemplate getTmeplateManager();
	
	HAPManagerCronJob getCronJobManager();
	
	HAPRuntime getRuntime();
	
}
