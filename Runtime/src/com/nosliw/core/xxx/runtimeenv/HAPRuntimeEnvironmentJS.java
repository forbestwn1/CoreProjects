package com.nosliw.core.xxx.runtimeenv;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="RUNTIME")
public abstract class HAPRuntimeEnvironmentJS implements HAPRuntimeEnvironment{

	@HAPAttribute
	public static final String NODENAME_GATEWAY = "gatewayObj";
	
	@HAPAttribute
	public static final String GATEWAY_CODETABLE = "codeTable";

//	@HAPAttribute
//	public static final String GATEWAY_RESOURCE = "resources";

//	@HAPAttribute
//	public static final String GATEWAY_PACKAGE = "package";

	@HAPAttribute
	public static final String GATEWAY_RESOURCEDEFINITION = "resourceDefinition";

	@HAPAttribute
	public static final String GATEWAY_CRITERIA = "criteria";
	
	@HAPAttribute
	public static final String GATEWAY_ERRORLOG = "errorLog";

/*	
	private HAPDataTypeManager m_dataTypeManager;
	
	private HAPManagerCodeTable m_codeTableManager;
	
	private HAPDataTypeHelper m_dataTypeHelper;
	
	private HAPManagerResource m_resourceManager;
	
	private HAPManagerTask m_taskManager;
	
	private HAPManagerActivity m_activityManager;
	
	private HAPManagerProcess m_processManager;
	
	private HAPRuntimeProcess m_processRuntime;
	
	private HAPParserDataExpression m_dataExpressionParser;
	
	private HAPGatewayManager m_gatewayManager;
	
	private HAPManagerService m_serviceManager;
	
	private HAPManagerResourceDefinition m_resourceDefinitionManager;
	
	private HAPManagerApplicationBrick m_brickManager;
	
	private HAPManagerUITag m_uiTagManager;
	
	private HAPManagerDynamicResource m_dynamicResourceManager;
	
	private HAPManagerCronJob m_cronJobManager;
	
	private HAPRuntime m_runtime;
	
	public HAPRuntimeEnvironmentJS(){}
	
	public HAPRuntimeEnvironmentJS(
			HAPDataTypeManager dataTypeManager,
			HAPDataTypeHelper dataTypeHelper,
			HAPManagerCodeTable codeTableManager,
			HAPManagerResource resourceMan,
			HAPManagerTask taskManager,
			HAPManagerActivity activityManager,
			HAPManagerProcess processManager,
			HAPRuntimeProcess processRuntime,
			HAPParserDataExpression dataExpressionParser,
		    HAPGatewayManager gatewayManager,
		    HAPManagerService serviceManager,
		    HAPManagerDynamicResource dynamicResourceManager,
		    HAPManagerResourceDefinition resourceDefManager,
			HAPManagerApplicationBrick brickManager,
			HAPManagerUITag uiTagManager, 
		    HAPManagerCronJob cronJobManager,
		    HAPRuntime runtime){
		super();
		this.init(dataTypeManager, dataTypeHelper, codeTableManager, resourceMan, taskManager, activityManager, processManager, processRuntime, dataExpressionParser, gatewayManager, serviceManager, dynamicResourceManager, resourceDefManager, brickManager, uiTagManager, cronJobManager, runtime);
	}
	
	protected void init(
				HAPDataTypeManager dataTypeManager,
				HAPDataTypeHelper dataTypeHelper,
				HAPManagerCodeTable codeTableManager,
				HAPManagerResource resourceMan,
				HAPManagerTask taskManager,
				HAPManagerActivity activityManager,
				HAPManagerProcess processManager,
				HAPRuntimeProcess processRuntime,
				HAPParserDataExpression dataExpressionParser,
			    HAPGatewayManager gatewayManager,
			    HAPManagerService serviceManager,
			    HAPManagerDynamicResource dynamicResourceManager,
			    HAPManagerResourceDefinition resourceDefManager,
				HAPManagerApplicationBrick brickManager,
				HAPManagerUITag uiTagManager,
			    HAPManagerCronJob cronJobManager,
			    HAPRuntime runtime){ 
		this.m_dataTypeManager = dataTypeManager;
		this.m_dataTypeHelper = dataTypeHelper;
		this.m_resourceManager = resourceMan;
		this.m_taskManager = taskManager;
		this.m_activityManager = activityManager;
		this.m_processManager = processManager;
		this.m_processRuntime = processRuntime;
		this.m_dataExpressionParser = dataExpressionParser;
		this.m_serviceManager = serviceManager;
		this.m_resourceDefinitionManager = resourceDefManager;
		this.m_brickManager = brickManager;
		this.m_uiTagManager = uiTagManager;
		this.m_dynamicResourceManager = dynamicResourceManager;
		this.m_cronJobManager = cronJobManager;
		this.m_codeTableManager = codeTableManager;

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
		
		//brick division
		this.getBrickManager().registerDivisionInfo(HAPConstantShared.BRICK_DIVISION_SERVICE, this.getServiceManager());
		this.getBrickManager().registerDivisionInfo(HAPConstantShared.BRICK_DIVISION_SCRIPT, new HAPPluginDivisionScript());

		
		
		//task
//		this.m_taskManager.registerTaskInfo(HAPConstantShared.TASK_TYPE_ACTIVITY, new HAPInfoTask(new HAPTaskInfoParserActivity(this.getActivityManager().getPluginManager()), new HAPTaskInfoProcessorActivity(this)));
//		this.m_taskManager.registerTaskInfo(HAPConstantShared.TASK_TYPE_SEQUENCE, new HAPInfoTask(new HAPTaskInfoParserSequence(this.getTaskManager()), new HAPTaskInfoProcessorSequence(this.getTaskManager())));
		
		
		//component

		//resource
		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.TEST_COMPLEX_1_100), new HAPPluginResourceManagerImpBrick(this));
		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100), new HAPPluginResourceManagerImpBrick(this));

		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.TASKWRAPPER_100), new HAPPluginResourceManagerImpBrick(this));
		
		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.DATAEXPRESSIONLIB_100), new HAPPluginResourceManagerImpBrick(this));
		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.DATAEXPRESSIONGROUP_100), new HAPPluginResourceManagerImpBrick(this));

		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100), new HAPPluginResourceManagerImpBrick(this));
		
		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.SERVICEPROFILE_100), new HAPPluginResourceManagerImpBrick(this));
		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.SERVICEINTERFACE_100), new HAPPluginResourceManagerImpBrick(this));

		
		this.m_resourceManager.registerResourceManagerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT), new HAPPluginResourceManagerScript(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT));
		this.m_resourceManager.registerResourceManagerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT), new HAPPluginResourceManagerImpBrick(this));

		
		this.m_resourceManager.registerResourceManagerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE), new HAPPluginResourceManagerConfigure());

		this.m_resourceManager.registerResourceManagerPlugin(HAPUtilityBrickId.getResourceTypeIdFromBrickTypeId(HAPEnumBrickType.UIPAGE_100), new HAPPluginResourceManagerImpBrick(this));

		
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, new HAPResourceManagerProcess(this.m_processManager, this.m_resourceManager));
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, new HAPResourceManagerActivityPlugin(this.m_activityManager.getPluginManager(), this.m_resourceManager));
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CRONJOB, new HAPResourceManagerCronJob(this.m_cronJobManager, this.m_resourceManager));
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CODETABLE, new HAPResourceManagerCodeTable(this.m_codeTableManager, this.m_resourceManager));
//
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE, new HAPResourceManagerImpConfigure(this.m_domainEntityDefinitionManager, this.m_resourceDefinitionManager, this.m_resourceManager));
//
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
//
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASK, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
//
//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));

		
		//resource definition
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, this));
//
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, this));
		
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, new HAPParserResourceValue()));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, this));
//
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASK, this));

//		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginServiceDefinition(this.getServiceManager().getServiceDefinitionManager()));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESSSUITE, new HAPParserResourceDefinitionProcess(this.getProcessManager().getPluginManager())));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionProcess(this.getResourceDefinitionManager()));

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginCronJob());

//		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginStory());

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, this));

		//domain entity definition
		
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginParserBrickImpTestComplex1(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginParserEntityImpTestComplexScript(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTestSimple1(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainAttachment(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginParserBrickImpValueContext(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginParserBrickImpValueStructure(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionDataGroup(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionDataGroupTemp(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionDataSingle(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionScriptGroup(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionScriptGroupTemp(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionScriptSingle(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainConfigure(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScript(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainDataAssociation(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainInteractive(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, HAPBrickServiceInterface1.class, this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainServiceProvider(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainDataAssociationInteractive(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainDataAssociationTask(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainData(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainValue(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainContainerComplex(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainContainerSimple(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTask(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScriptTaskGroup(this));
//
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, true, this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, false, this));


		//complex entity processor
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginProcessorBrickDefinitionComplexImpTestComplex1());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexTestComplexScript());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionDataGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP));
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionDataGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP));
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionDataSingle());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionScriptGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP));
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionScriptGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP));
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionScriptSingle());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorContainerComplex());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorContainerSimple());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorTask());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorScriptTaskGroup());
//
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT));
//
//		//simple entity processor
//		this.getDomainEntityExecutableManager().registerSimpleEntityProcessorPlugin(new HAPPluginSimpleEntityProcessorInteractive(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE));
//		this.getDomainEntityExecutableManager().registerSimpleEntityProcessorPlugin(new HAPPluginSimpleEntityProcessorServiceProvider());
//		this.getDomainEntityExecutableManager().registerSimpleEntityProcessorPlugin(new HAPPluginEntityProcessorSimpleScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT));
//		
//		//adapter entity
//		this.getDomainEntityExecutableManager().registerAdapterProcessorPlugin(new HAPPluginAdapterProcessorDataAssociation());
//		this.getDomainEntityExecutableManager().registerAdapterProcessorPlugin(new HAPPluginAdapterProcessorDataAssociationInteractive());
//		this.getDomainEntityExecutableManager().registerAdapterProcessorPlugin(new HAPPluginAdapterProcessorDataAssociationTask());
//		
//		//runtime
//		this.m_runtime = runtime;
//		this.m_runtime.start();
	}
	
	@Override
	public HAPDataTypeManager getDataTypeManager() {  return this.m_dataTypeManager;   }

	@Override
	public HAPManagerCodeTable getCodeTableManager() {   return this.m_codeTableManager;      }

	@Override
	public HAPManagerResource getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPManagerTask getTaskManager() {    return this.m_taskManager;    }

	@Override
	public HAPManagerActivity getActivityManager() {   return this.m_activityManager;    }

	@Override
	public HAPManagerProcess getProcessManager() {  return this.m_processManager;  }

	@Override
	public HAPRuntimeProcess getProcessRuntime() {   return this.m_processRuntime;  }

	@Override
	public HAPParserDataExpression getDataExpressionParser() {    return this.m_dataExpressionParser;    }

	@Override
	public HAPGatewayManager getGatewayManager(){  return this.m_gatewayManager;   }

	@Override
	public HAPManagerService getServiceManager() {  return this.m_serviceManager;   }

	@Override
	public HAPManagerResourceDefinition getResourceDefinitionManager() {  return this.m_resourceDefinitionManager;  }

	@Override
	public HAPManagerApplicationBrick getBrickManager() {   return this.m_brickManager;  }

	@Override
	public HAPManagerUITag getUITagManager() {   return this.m_uiTagManager;     }
	
	@Override
	public HAPManagerDynamicResource getDynamicResourceManager() {  return this.m_dynamicResourceManager;	}

	@Override
	public HAPManagerCronJob getCronJobManager() {  return this.m_cronJobManager;	}

	@Override
	public HAPRuntime getRuntime() {		return this.m_runtime;	}
	
	@Override
	public HAPDataTypeHelper getDataTypeHelper() {   return this.m_dataTypeHelper;   }

*/
}
