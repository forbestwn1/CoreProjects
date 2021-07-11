package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.activity.HAPProcessorAttachmentEntityActivity;
import com.nosliw.data.core.activity.HAPTaskInfoParserActivity;
import com.nosliw.data.core.activity.HAPTaskInfoProcessorActivity;
import com.nosliw.data.core.activity.resource.HAPParserResourceDefinitionActivitySuite;
import com.nosliw.data.core.activity.resource.HAPResourceManagerActivityPlugin;
import com.nosliw.data.core.codetable.HAPGatewayCodeTable;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.codetable.HAPResourceManagerCodeTable;
import com.nosliw.data.core.component.attachment.HAPManagerAttachment;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.cronjob.HAPResourceDefinitionPluginCronJob;
import com.nosliw.data.core.cronjob.HAPResourceManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.err.HAPGatewayErrorLogger;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.expression.HAPProcessorAttachmentEntityExpression;
import com.nosliw.data.core.expression.resource.HAPParserResourceDefinitionExpressionSuite;
import com.nosliw.data.core.expression.resource.HAPPluginResourceDefinitionExpressionGroup;
import com.nosliw.data.core.expression.resource.HAPResourceManagerExpression;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPRuntimeProcess;
import com.nosliw.data.core.process1.resource.HAPParserResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPPluginResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceManagerProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayCriteriaOperation;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResource;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResourceDefinition;
import com.nosliw.data.core.script.expression.HAPManagerScript;
import com.nosliw.data.core.script.expression.resource.HAPParserResourceDefinitionScriptGroup;
import com.nosliw.data.core.sequence.HAPTaskInfoParserSequence;
import com.nosliw.data.core.sequence.HAPTaskInfoProcessorSequence;
import com.nosliw.data.core.service.definition.HAPFactoryServiceProcess;
import com.nosliw.data.core.service.definition.HAPManagerService;
import com.nosliw.data.core.service.resource.HAPParserServiceInterfaceResource;
import com.nosliw.data.core.service.resource.HAPResourceDefinitionPluginServiceDefinition;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionPluginStory;
import com.nosliw.data.core.task.HAPInfoTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.resource.HAPParserResourceDefinitionTaskSuite;
import com.nosliw.data.core.value.HAPParserResourceValue;
import com.nosliw.data.core.valuestructure.HAPProcessorAttachmentEntityValueStructure;
import com.nosliw.data.core.valuestructure.resource.HAPParserResourceDefinitionStructure;

@HAPEntityWithAttribute(baseName="RUNTIME")
public abstract class HAPRuntimeEnvironmentJS implements HAPRuntimeEnvironment{

	@HAPAttribute
	public static final String NODENAME_GATEWAY = "gatewayObj";
	
	@HAPAttribute
	public static final String GATEWAY_CODETABLE = "codeTable";

	@HAPAttribute
	public static final String GATEWAY_RESOURCE = "resources";

	@HAPAttribute
	public static final String GATEWAY_RESOURCEDEFINITION = "resourceDefinition";

	@HAPAttribute
	public static final String GATEWAY_CRITERIA = "criteria";
	
	@HAPAttribute
	public static final String GATEWAY_ERRORLOG = "errorLog";

	private HAPDataTypeManager m_dataTypeManager;
	
	private HAPManagerCodeTable m_codeTableManager;
	
	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPResourceManagerRoot m_resourceManager;
	
	private HAPManagerTask m_taskManager;
	
	private HAPManagerActivity m_activityManager;
	
	private HAPManagerProcess m_processManager;
	
	private HAPRuntimeProcess m_processRuntime;
	
	private HAPManagerExpression m_expressionManager;
	
	private HAPManagerScript m_scriptManager;
	
	private HAPGatewayManager m_gatewayManager;
	
	private HAPManagerService m_serviceManager;
	
	private HAPManagerAttachment m_attachmentManager;
	
	private HAPManagerResourceDefinition m_resourceDefinitionManager;
	
	private HAPManagerDynamicResource m_dynamicResourceManager;
	
	private HAPManagerCronJob m_cronJobManager;
	
	private HAPManagerStory m_storyManager;
	
	private HAPRuntime m_runtime;
	
	public HAPRuntimeEnvironmentJS(){}
	
	public HAPRuntimeEnvironmentJS(
			HAPDataTypeManager dataTypeManager,
			HAPDataTypeHelper dataTypeHelper,
			HAPManagerCodeTable codeTableManager,
			HAPResourceManagerRoot resourceMan,
			HAPManagerTask taskManager,
			HAPManagerActivity activityManager,
			HAPManagerProcess processManager,
			HAPRuntimeProcess processRuntime,
			HAPManagerExpression expressionManager,
			HAPManagerScript scriptManager,
		    HAPGatewayManager gatewayManager,
		    HAPManagerService serviceManager,
		    HAPManagerDynamicResource dynamicResourceManager,
		    HAPManagerResourceDefinition resourceDefManager,
			HAPManagerAttachment attachmentManager,
		    HAPManagerCronJob cronJobManager,
		    HAPManagerStory storyManager,
		    HAPRuntime runtime){
		super();
		this.init(dataTypeManager, dataTypeHelper, codeTableManager, resourceMan, taskManager, activityManager, processManager, processRuntime, expressionManager, scriptManager, gatewayManager, serviceManager, dynamicResourceManager, resourceDefManager, attachmentManager, cronJobManager, storyManager, runtime);
	}
	
	protected void init(
				HAPDataTypeManager dataTypeManager,
				HAPDataTypeHelper dataTypeHelper,
				HAPManagerCodeTable codeTableManager,
				HAPResourceManagerRoot resourceMan,
				HAPManagerTask taskManager,
				HAPManagerActivity activityManager,
				HAPManagerProcess processManager,
				HAPRuntimeProcess processRuntime,
				HAPManagerExpression expressionManager,
				HAPManagerScript scriptManager,
			    HAPGatewayManager gatewayManager,
			    HAPManagerService serviceManager,
			    HAPManagerDynamicResource dynamicResourceManager,
			    HAPManagerResourceDefinition resourceDefManager,
				HAPManagerAttachment attachmentManager,
			    HAPManagerCronJob cronJobManager,
			    HAPManagerStory storyManager,
			    HAPRuntime runtime){ 
		this.m_dataTypeManager = dataTypeManager;
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_resourceManager = resourceMan;
		this.m_taskManager = taskManager;
		this.m_activityManager = activityManager;
		this.m_processManager = processManager;
		this.m_processRuntime = processRuntime;
		this.m_expressionManager = expressionManager;
		this.m_scriptManager = scriptManager;
		this.m_serviceManager = serviceManager;
		this.m_resourceDefinitionManager = resourceDefManager;
		this.m_attachmentManager = attachmentManager;
		this.m_dynamicResourceManager = dynamicResourceManager;
		this.m_storyManager = storyManager;
		this.m_cronJobManager = cronJobManager;
		this.m_codeTableManager = codeTableManager;

		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, new HAPResourceManagerExpression(this.m_expressionManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, new HAPResourceManagerProcess(this.m_processManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, new HAPResourceManagerActivityPlugin(this.m_activityManager.getPluginManager(), this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CRONJOB, new HAPResourceManagerCronJob(this.m_cronJobManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CODETABLE, new HAPResourceManagerCodeTable(this.m_codeTableManager, this.m_resourceManager));

		
//		this.m_dataSourceManager.registerDataSourceFactory(HAPDataSourceFactoryTask.FACTORY_TYPE, new HAPDataSourceFactoryTask(this.getTaskManager()));
//		this.getTaskManager().registerTaskManager(HAPConstant.DATATASK_TYPE_DATASOURCE, new HAPManagerTaskDatasource(this.getDataSourceManager().getDataSourceDefinitionManager(), this.getDataSourceManager(), runtime));
		
		//gateway
		this.m_gatewayManager = gatewayManager;
		this.getGatewayManager().registerGateway(GATEWAY_RESOURCEDEFINITION, new HAPGatewayResourceDefinition(this));
		this.getGatewayManager().registerGateway(GATEWAY_RESOURCE, new HAPGatewayResource(this));
		this.getGatewayManager().registerGateway(GATEWAY_CRITERIA, new HAPGatewayCriteriaOperation());
		this.getGatewayManager().registerGateway(GATEWAY_ERRORLOG, new HAPGatewayErrorLogger());
		this.getGatewayManager().registerGateway(GATEWAY_CODETABLE, new HAPGatewayCodeTable(this.m_codeTableManager));
		
		//service factory
		
		this.m_serviceManager.registerServiceFactory(HAPFactoryServiceProcess.FACTORY_TYPE, new HAPFactoryServiceProcess(this.m_processRuntime, this.m_processManager, this.m_resourceManager));
		
		//task
		this.m_taskManager.registerTaskInfo(HAPConstantShared.TASK_TYPE_ACTIVITY, new HAPInfoTask(new HAPTaskInfoParserActivity(this.getActivityManager().getPluginManager()), new HAPTaskInfoProcessorActivity(this)));
		this.m_taskManager.registerTaskInfo(HAPConstantShared.TASK_TYPE_SEQUENCE, new HAPInfoTask(new HAPTaskInfoParserSequence(this.getTaskManager()), new HAPTaskInfoProcessorSequence(this.getTaskManager())));
		
		//attachment
		this.m_attachmentManager.registerProcessor(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSION, new HAPProcessorAttachmentEntityExpression(this));
		this.m_attachmentManager.registerProcessor(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, new HAPProcessorAttachmentEntityValueStructure());
		this.m_attachmentManager.registerProcessor(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYSUITE, new HAPProcessorAttachmentEntityActivity(this.getActivityManager().getPluginManager()));
		
		//component
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, new HAPParserResourceValue()));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE, new HAPParserResourceDefinitionExpressionSuite()));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionExpressionGroup(this.getResourceDefinitionManager()));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSION, new HAPParserResourceDefinitionScriptGroup()));

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginServiceDefinition(this.getServiceManager().getServiceDefinitionManager()));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYSUITE, new HAPParserResourceDefinitionActivitySuite(this.getActivityManager().getPluginManager())));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESSSUITE, new HAPParserResourceDefinitionProcess(this.getProcessManager().getPluginManager())));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionProcess(this.getResourceDefinitionManager()));

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginCronJob());

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginStory());

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, new HAPParserResourceDefinitionStructure()));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImp(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, new HAPParserServiceInterfaceResource()));

		//runtime
		this.m_runtime = runtime;
		this.m_runtime.start();
	}
	
	@Override
	public HAPDataTypeManager getDataTypeManager() {  return this.m_dataTypeManager;   }

	@Override
	public HAPManagerCodeTable getCodeTableManager() {   return this.m_codeTableManager;      }

	@Override
	public HAPResourceManagerRoot getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPManagerTask getTaskManager() {    return this.m_taskManager;    }

	@Override
	public HAPManagerActivity getActivityManager() {   return this.m_activityManager;    }

	@Override
	public HAPManagerProcess getProcessManager() {  return this.m_processManager;  }

	@Override
	public HAPRuntimeProcess getProcessRuntime() {   return this.m_processRuntime;  }

	@Override
	public HAPManagerExpression getExpressionManager(){  return this.m_expressionManager;  }

	@Override
	public HAPManagerScript getScriptManager() {    return this.m_scriptManager;    }

	@Override
	public HAPGatewayManager getGatewayManager(){  return this.m_gatewayManager;   }

	@Override
	public HAPManagerService getServiceManager() {  return this.m_serviceManager;   }

	@Override
	public HAPManagerResourceDefinition getResourceDefinitionManager() {  return this.m_resourceDefinitionManager;  }

	@Override
	public HAPManagerAttachment getAttachmentManager() {    return this.m_attachmentManager;     }

	@Override
	public HAPManagerDynamicResource getDynamicResourceManager() {  return this.m_dynamicResourceManager;	}

	@Override
	public HAPManagerCronJob getCronJobManager() {  return this.m_cronJobManager;	}

	@Override
	public HAPManagerStory getStoryManager() {  return this.m_storyManager;   }
	
	@Override
	public HAPRuntime getRuntime() {		return this.m_runtime;	}
	
	@Override
	public HAPDataTypeHelper getDataTypeHelper() {   return this.m_dataTypeHelper;   }


}
