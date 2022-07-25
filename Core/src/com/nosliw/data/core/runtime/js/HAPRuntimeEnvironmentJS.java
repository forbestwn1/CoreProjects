package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.activity.HAPProcessorAttachmentEntityActivity;
import com.nosliw.data.core.activity.HAPResourceManagerActivityPlugin;
import com.nosliw.data.core.activity.HAPTaskInfoParserActivity;
import com.nosliw.data.core.activity.HAPTaskInfoProcessorActivity;
import com.nosliw.data.core.codetable.HAPGatewayCodeTable;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.codetable.HAPResourceManagerCodeTable;
import com.nosliw.data.core.complex.HAPManagerComplexEntity;
import com.nosliw.data.core.complex.HAPResourceManagerImpComplex;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.cronjob.HAPResourceDefinitionPluginCronJob;
import com.nosliw.data.core.cronjob.HAPResourceManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPPluginResourceDefinitionImpEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPManagerAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPPluginEntityDefinitionInDomainAttachment;
import com.nosliw.data.core.domain.entity.expression.HAPManagerExpression;
import com.nosliw.data.core.domain.entity.expression.HAPPluginComplexEntityProcessorExpression;
import com.nosliw.data.core.domain.entity.expression.HAPPluginComplexEntityProcessorExpressionSuite;
import com.nosliw.data.core.domain.entity.expression.HAPPluginEntityDefinitionInDomainExpression;
import com.nosliw.data.core.domain.entity.expression.HAPPluginEntityDefinitionInDomainExpressionSuite;
import com.nosliw.data.core.domain.entity.expression.HAPProcessorAttachmentEntityExpression;
import com.nosliw.data.core.domain.entity.expression.resource.HAPPluginResourceDefinitionExpressionGroup;
import com.nosliw.data.core.domain.entity.expression.resource.HAPResourceManagerExpression;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPluginEntityDefinitionInDomainValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPluginEntityDefinitionInDomainValueStructureComplex;
import com.nosliw.data.core.domain.testing.HAPPluginEntityDefinitionInDomainDynamic;
import com.nosliw.data.core.domain.testing.testcomplex1.HAPPluginComplexEntityProcessorTestComplex1;
import com.nosliw.data.core.domain.testing.testcomplex1.HAPPluginEntityDefinitionInDomainTestComplex1;
import com.nosliw.data.core.domain.testing.testdecoration1.HAPPluginComplexEntityProcessorTestDecoration1;
import com.nosliw.data.core.domain.testing.testdecoration1.HAPPluginEntityDefinitionInDomainTestDecoration1;
import com.nosliw.data.core.domain.testing.testdecoration1.HAPPluginResourceDefinitionImpTestDecoration1;
import com.nosliw.data.core.domain.testing.testsimple1.HAPDefinitionEntityTestSimple1;
import com.nosliw.data.core.err.HAPGatewayErrorLogger;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPRuntimeProcess;
import com.nosliw.data.core.process1.resource.HAPPluginResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceManagerProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayCriteriaOperation;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayPackage;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResource;
import com.nosliw.data.core.runtime.js.gateway.HAPGatewayResourceDefinition;
import com.nosliw.data.core.script.expression.HAPManagerScript;
import com.nosliw.data.core.sequence.HAPTaskInfoParserSequence;
import com.nosliw.data.core.sequence.HAPTaskInfoProcessorSequence;
import com.nosliw.data.core.service.definition.HAPFactoryServiceProcess;
import com.nosliw.data.core.service.definition.HAPManagerService;
import com.nosliw.data.core.service.resource.HAPResourceDefinitionPluginServiceDefinition;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionPluginStory;
import com.nosliw.data.core.task.HAPInfoTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.valuestructure.HAPProcessorAttachmentEntityValueStructure;

@HAPEntityWithAttribute(baseName="RUNTIME")
public abstract class HAPRuntimeEnvironmentJS implements HAPRuntimeEnvironment{

	@HAPAttribute
	public static final String NODENAME_GATEWAY = "gatewayObj";
	
	@HAPAttribute
	public static final String GATEWAY_CODETABLE = "codeTable";

	@HAPAttribute
	public static final String GATEWAY_RESOURCE = "resources";

	@HAPAttribute
	public static final String GATEWAY_PACKAGE = "package";

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
	
	private HAPManagerDomainEntityDefinition m_domainEntityManager;
	
	private HAPManagerComplexEntity m_complexEntityManager;
 	
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
		    HAPManagerDomainEntityDefinition domainEntityManager,
			HAPManagerComplexEntity complexEntityManager,
			HAPManagerAttachment attachmentManager,
		    HAPManagerCronJob cronJobManager,
		    HAPManagerStory storyManager,
		    HAPRuntime runtime){
		super();
		this.init(dataTypeManager, dataTypeHelper, codeTableManager, resourceMan, taskManager, activityManager, processManager, processRuntime, expressionManager, scriptManager, gatewayManager, serviceManager, dynamicResourceManager, resourceDefManager, domainEntityManager, complexEntityManager, attachmentManager, cronJobManager, storyManager, runtime);
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
			    HAPManagerDomainEntityDefinition domainEntityManager,
				HAPManagerComplexEntity complexEntityManager,
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
		this.m_domainEntityManager = domainEntityManager;
		this.m_complexEntityManager = complexEntityManager;
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
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1, new HAPResourceManagerImpComplex(this.m_complexEntityManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_DECORATION1, new HAPResourceManagerImpComplex(this.m_complexEntityManager, this.m_resourceManager));

//		this.m_dataSourceManager.registerDataSourceFactory(HAPDataSourceFactoryTask.FACTORY_TYPE, new HAPDataSourceFactoryTask(this.getTaskManager()));
//		this.getTaskManager().registerTaskManager(HAPConstant.DATATASK_TYPE_DATASOURCE, new HAPManagerTaskDatasource(this.getDataSourceManager().getDataSourceDefinitionManager(), this.getDataSourceManager(), runtime));
		
		//gateway
		this.m_gatewayManager = gatewayManager;
		this.getGatewayManager().registerGateway(GATEWAY_PACKAGE, new HAPGatewayPackage(this));
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
		this.m_attachmentManager.registerProcessor(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITY, new HAPProcessorAttachmentEntityActivity(this.getActivityManager().getPluginManager()));
		
		//component

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX1, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpTestDecoration1(this));
		
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, new HAPParserResourceValue()));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionExpressionGroup(this.getResourceDefinitionManager()));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSION, new HAPParserResourceDefinitionScriptGroup()));

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginServiceDefinition(this.getServiceManager().getServiceDefinitionManager()));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESSSUITE, new HAPParserResourceDefinitionProcess(this.getProcessManager().getPluginManager())));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionProcess(this.getResourceDefinitionManager()));

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginCronJob());

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginStory());

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, new HAPParserServiceInterfaceResource()));

		//domain entity
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTestComplex1(this));
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTestDecoration1(this));
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainDynamic(HAPDefinitionEntityTestSimple1.class, false, this));
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainAttachment(this));
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainValueStructureComplex(this));
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainValueStructure(this));
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionSuite(this));
		this.getDomainEntityManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpression(this));

		//complex entity
		this.getComplexEntityManager().registerProcessorPlugin(new HAPPluginComplexEntityProcessorTestComplex1());
		this.getComplexEntityManager().registerProcessorPlugin(new HAPPluginComplexEntityProcessorExpression());
		this.getComplexEntityManager().registerProcessorPlugin(new HAPPluginComplexEntityProcessorTestDecoration1());
		this.getComplexEntityManager().registerProcessorPlugin(new HAPPluginComplexEntityProcessorExpressionSuite());
		
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
	public HAPManagerDomainEntityDefinition getDomainEntityManager() {  return this.m_domainEntityManager;  }

	@Override
	public HAPManagerComplexEntity getComplexEntityManager() {   return this.m_complexEntityManager;   }

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
