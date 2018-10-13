package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.datasource.HAPDataSourceFactoryTask;
import com.nosliw.data.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.datasource.task.HAPManagerTaskDatasource;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayCriteriaOperation;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayExpressionDiscovery;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResource;
import com.nosliw.data.core.service1.HAPManagerService;
import com.nosliw.data.core.task.HAPManagerTask;

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

	private HAPResourceManagerRoot m_resourceManager;
	
	private HAPManagerTask m_taskManager;
	
	private HAPExpressionSuiteManager m_expressionSuiteManager;
	
	private HAPGatewayManager m_gatewayManager;
	
	private HAPDataSourceManager m_dataSourceManager;
	
	private HAPManagerService m_serviceManager;
	
	private HAPRuntime m_runtime;
	
	public HAPRuntimeEnvironmentJS(){}
	
	public HAPRuntimeEnvironmentJS(HAPResourceManagerRoot resourceMan,
									HAPManagerTask taskManager,
									HAPExpressionSuiteManager expressionSuiteManager,
								    HAPGatewayManager gatewayManager,
								    HAPDataSourceManager dataSourceManager,
								    HAPManagerService serviceManager,
								    HAPRuntime runtime){
		super();
		this.init(resourceMan, taskManager, expressionSuiteManager, gatewayManager, dataSourceManager, serviceManager, runtime);
	}
	
	protected void init(HAPResourceManagerRoot resourceMan,
						HAPManagerTask taskManager,
						HAPExpressionSuiteManager expressionSuiteManager,
					    HAPGatewayManager gatewayManager,
					    HAPDataSourceManager dataSourceManager,
					    HAPManagerService serviceManager,
					    HAPRuntime runtime){ 
		this.m_resourceManager = resourceMan;
		this.m_taskManager = taskManager;
		this.m_expressionSuiteManager = expressionSuiteManager;
		this.m_dataSourceManager = dataSourceManager;
		this.m_serviceManager = serviceManager;

//		this.m_dataSourceManager.registerDataSourceFactory(HAPDataSourceFactoryTask.FACTORY_TYPE, new HAPDataSourceFactoryTask(this.getTaskManager()));
//		this.getTaskManager().registerTaskManager(HAPConstant.DATATASK_TYPE_DATASOURCE, new HAPManagerTaskDatasource(this.getDataSourceManager().getDataSourceDefinitionManager(), this.getDataSourceManager(), runtime));
		
		//gateway
		this.m_gatewayManager = gatewayManager;
		this.getGatewayManager().registerGateway(GATEWAY_RESOURCE, new HAPGatewayResource(this));
		this.getGatewayManager().registerGateway(GATEWAY_CRITERIA, new HAPGatewayCriteriaOperation());
		this.getGatewayManager().registerGateway(GATEWAY_DISCOVERY, new HAPGatewayExpressionDiscovery(this.getExpressionSuiteManager(), runtime));
		
		//runtime
		this.m_runtime = runtime;
		this.m_runtime.start();
	}
	
	@Override
	public HAPResourceManagerRoot getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPManagerTask getTaskManager() {  return this.m_taskManager;  }

	@Override
	public HAPExpressionSuiteManager getExpressionSuiteManager(){  return this.m_expressionSuiteManager;  }

	@Override
	public HAPGatewayManager getGatewayManager(){  return this.m_gatewayManager;   }

	@Override
	public HAPDataSourceManager getDataSourceManager() {  return this.m_dataSourceManager;  }

	@Override
	public HAPManagerService getServiceManager() {  return this.m_serviceManager;   }

	
	@Override
	public HAPRuntime getRuntime() {		return this.m_runtime;	}

}
