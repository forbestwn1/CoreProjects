package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPManagerComponent;
import com.nosliw.data.core.err.HAPGatewayErrorLogger;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.resource.HAPResourceManagerActivityPlugin;
import com.nosliw.data.core.process.resource.HAPResourceManagerProcess;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayCriteriaOperation;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayExpressionDiscovery;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResource;
import com.nosliw.data.core.service.provide.HAPFactoryServiceProcess;
import com.nosliw.data.core.service.provide.HAPManagerService;

@HAPEntityWithAttribute(baseName="RUNTIME")
public abstract class HAPRuntimeEnvironmentJS implements HAPRuntimeEnvironment{

	@HAPAttribute
	public static final String NODENAME_GATEWAY = "gatewayObj";
	
	@HAPAttribute
	public static final String GATEWAY_RESOURCE = "resources";

	@HAPAttribute
	public static final String GATEWAY_CRITERIA = "criteria";
	
	@HAPAttribute
	public static final String GATEWAY_DISCOVERY = "discovery";

	@HAPAttribute
	public static final String GATEWAY_ERRORLOG = "errorLog";

	private HAPResourceManagerRoot m_resourceManager;
	
	private HAPManagerProcessDefinition m_processDefinitionManager;
	
	private HAPManagerProcess m_processManager;
	
	private HAPExpressionSuiteManager m_expressionSuiteManager;
	
	private HAPGatewayManager m_gatewayManager;
	
	private HAPManagerService m_serviceManager;
	
	private HAPManagerComponent m_componentManager;
	
	private HAPRuntime m_runtime;
	
	public HAPRuntimeEnvironmentJS(){}
	
	public HAPRuntimeEnvironmentJS(HAPResourceManagerRoot resourceMan,
									HAPManagerProcessDefinition processDefManager,
									HAPManagerProcess processManager,
									HAPExpressionSuiteManager expressionSuiteManager,
								    HAPGatewayManager gatewayManager,
								    HAPManagerService serviceManager,
								    HAPManagerComponent componentManager,
								    HAPRuntime runtime){
		super();
		this.init(resourceMan, processDefManager, processManager, expressionSuiteManager, gatewayManager, serviceManager, componentManager, runtime);
	}
	
	protected void init(HAPResourceManagerRoot resourceMan,
						HAPManagerProcessDefinition processDefManager,
						HAPManagerProcess processManager,
						HAPExpressionSuiteManager expressionSuiteManager,
					    HAPGatewayManager gatewayManager,
					    HAPManagerService serviceManager,
					    HAPManagerComponent componentManager,
					    HAPRuntime runtime){ 
		this.m_resourceManager = resourceMan;
		this.m_processDefinitionManager = processDefManager;
		this.m_processManager = processManager;
		this.m_expressionSuiteManager = expressionSuiteManager;
		this.m_serviceManager = serviceManager;
		this.m_componentManager = componentManager;

		this.m_resourceManager.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS, new HAPResourceManagerProcess(this.m_processDefinitionManager));
		this.m_resourceManager.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, new HAPResourceManagerActivityPlugin(this.m_processDefinitionManager.getPluginManager()));

		
//		this.m_dataSourceManager.registerDataSourceFactory(HAPDataSourceFactoryTask.FACTORY_TYPE, new HAPDataSourceFactoryTask(this.getTaskManager()));
//		this.getTaskManager().registerTaskManager(HAPConstant.DATATASK_TYPE_DATASOURCE, new HAPManagerTaskDatasource(this.getDataSourceManager().getDataSourceDefinitionManager(), this.getDataSourceManager(), runtime));
		
		//gateway
		this.m_gatewayManager = gatewayManager;
		this.getGatewayManager().registerGateway(GATEWAY_RESOURCE, new HAPGatewayResource(this));
		this.getGatewayManager().registerGateway(GATEWAY_CRITERIA, new HAPGatewayCriteriaOperation());
		this.getGatewayManager().registerGateway(GATEWAY_DISCOVERY, new HAPGatewayExpressionDiscovery(this.getExpressionSuiteManager(), runtime));
		this.getGatewayManager().registerGateway(GATEWAY_ERRORLOG, new HAPGatewayErrorLogger());
		
		//service factory
		
		this.m_serviceManager.registerServiceFactory(HAPFactoryServiceProcess.FACTORY_TYPE, new HAPFactoryServiceProcess(this.m_processManager, this.m_processDefinitionManager));
		
		//runtime
		this.m_runtime = runtime;
		this.m_runtime.start();
	}
	
	@Override
	public HAPResourceManagerRoot getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPManagerProcessDefinition getProcessDefinitionManager() {  return this.m_processDefinitionManager;  }

	@Override
	public HAPManagerProcess getProcessManager() {   return this.m_processManager;  }

	@Override
	public HAPExpressionSuiteManager getExpressionSuiteManager(){  return this.m_expressionSuiteManager;  }

	@Override
	public HAPGatewayManager getGatewayManager(){  return this.m_gatewayManager;   }

	@Override
	public HAPManagerService getServiceManager() {  return this.m_serviceManager;   }

	@Override
	public HAPManagerComponent getComponentManager() {  return this.m_componentManager;  }

	@Override
	public HAPRuntime getRuntime() {		return this.m_runtime;	}

}
