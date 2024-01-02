package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.activity.HAPResourceManagerActivityPlugin;
import com.nosliw.data.core.activity.HAPTaskInfoParserActivity;
import com.nosliw.data.core.activity.HAPTaskInfoProcessorActivity;
import com.nosliw.data.core.codetable.HAPGatewayCodeTable;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.codetable.HAPResourceManagerCodeTable;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.cronjob.HAPResourceDefinitionPluginCronJob;
import com.nosliw.data.core.cronjob.HAPResourceManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.domain.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPPluginResourceDefinitionImpEntity;
import com.nosliw.data.core.domain.HAPPluginResourceDefinitionImpEntityThin;
import com.nosliw.data.core.domain.common.interactive.HAPPluginEntityDefinitionInDomainInteractive;
import com.nosliw.data.core.domain.common.interactive.HAPPluginSimpleEntityProcessorInteractive;
import com.nosliw.data.core.domain.common.script.HAPPluginEntityDefinitionInDomainScriptBased;
import com.nosliw.data.core.domain.common.script.HAPPluginEntityProcessorComplexScriptBased;
import com.nosliw.data.core.domain.common.script.HAPPluginEntityProcessorSimpleScriptBased;
import com.nosliw.data.core.domain.common.script.HAPResourceManagerImpScriptBased;
import com.nosliw.data.core.domain.entity.HAPManagerDomainEntityExecutable;
import com.nosliw.data.core.domain.entity.HAPResourceManagerImpComplex;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPPluginAdapterProcessorDataAssociation;
import com.nosliw.data.core.domain.entity.adapter.dataassociation.HAPPluginEntityDefinitionInDomainDataAssociation;
import com.nosliw.data.core.domain.entity.adapter.interactive.HAPPluginAdapterProcessorDataAssociationInteractive;
import com.nosliw.data.core.domain.entity.adapter.interactive.HAPPluginEntityDefinitionInDomainDataAssociationInteractive;
import com.nosliw.data.core.domain.entity.adapter.task.HAPPluginAdapterProcessorDataAssociationTask;
import com.nosliw.data.core.domain.entity.adapter.task.HAPPluginEntityDefinitionInDomainDataAssociationTask;
import com.nosliw.data.core.domain.entity.attachment.HAPPluginEntityDefinitionInDomainAttachment;
import com.nosliw.data.core.domain.entity.configure.HAPPluginEntityDefinitionInDomainConfigure;
import com.nosliw.data.core.domain.entity.configure.HAPResourceManagerImpConfigure;
import com.nosliw.data.core.domain.entity.container.HAPPluginEntityDefinitionInDomainContainerComplex;
import com.nosliw.data.core.domain.entity.container.HAPPluginEntityDefinitionInDomainContainerSimple;
import com.nosliw.data.core.domain.entity.container.HAPPluginEntityProcessorContainerComplex;
import com.nosliw.data.core.domain.entity.container.HAPPluginEntityProcessorContainerSimple;
import com.nosliw.data.core.domain.entity.data.HAPPluginEntityDefinitionInDomainData;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserDataExpression;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityDefinitionInDomainExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityDefinitionInDomainExpressionDataGroupTemp;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityDefinitionInDomainExpressionDataSingle;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityProcessorComplexExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityProcessorComplexExpressionDataSingle;
import com.nosliw.data.core.domain.entity.expression.script.HAPPluginEntityDefinitionInDomainExpressionScriptGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPPluginEntityDefinitionInDomainExpressionScriptGroupTemp;
import com.nosliw.data.core.domain.entity.expression.script.HAPPluginEntityDefinitionInDomainExpressionScriptSingle;
import com.nosliw.data.core.domain.entity.expression.script.HAPPluginEntityProcessorComplexExpressionScriptGroup;
import com.nosliw.data.core.domain.entity.expression.script.HAPPluginEntityProcessorComplexExpressionScriptSingle;
import com.nosliw.data.core.domain.entity.script.task.HAPPluginEntityDefinitionInDomainScriptTaskGroup;
import com.nosliw.data.core.domain.entity.script.task.HAPPluginEntityProcessorScriptTaskGroup;
import com.nosliw.data.core.domain.entity.service.provider.HAPPluginEntityDefinitionInDomainServiceProvider;
import com.nosliw.data.core.domain.entity.service.provider.HAPPluginSimpleEntityProcessorServiceProvider;
import com.nosliw.data.core.domain.entity.task.HAPPluginEntityDefinitionInDomainTask;
import com.nosliw.data.core.domain.entity.task.HAPPluginEntityProcessorTask;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPPluginEntityDefinitionInDomainTestComplexScript;
import com.nosliw.data.core.domain.entity.test.complex.script.HAPPluginEntityProcessorComplexTestComplexScript;
import com.nosliw.data.core.domain.entity.test.complex.testcomplex1.HAPPluginEntityDefinitionInDomainTestComplex1;
import com.nosliw.data.core.domain.entity.test.complex.testcomplex1.HAPPluginEntityProcessorComplexTestComplex1;
import com.nosliw.data.core.domain.entity.test.simple.testsimple1.HAPPluginEntityDefinitionInDomainTestSimple1;
import com.nosliw.data.core.domain.entity.value.HAPPluginEntityDefinitionInDomainValue;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPluginEntityDefinitionInDomainValueContext;
import com.nosliw.data.core.domain.entity.valuestructure.HAPPluginEntityDefinitionInDomainValueStructure;
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
import com.nosliw.data.core.script.expression1.HAPManagerScript;
import com.nosliw.data.core.sequence.HAPTaskInfoParserSequence;
import com.nosliw.data.core.sequence.HAPTaskInfoProcessorSequence;
import com.nosliw.data.core.service.definition.HAPFactoryServiceProcess;
import com.nosliw.data.core.service.definition.HAPManagerService;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.resource.HAPResourceDefinitionPluginServiceDefinition;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionPluginStory;
import com.nosliw.data.core.task.HAPInfoTask;
import com.nosliw.data.core.task.HAPManagerTask;

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
	
	private HAPParserDataExpression m_dataExpressionParser;
	
	private HAPManagerScript m_scriptManager;
	
	private HAPGatewayManager m_gatewayManager;
	
	private HAPManagerService m_serviceManager;
	
	private HAPManagerResourceDefinition m_resourceDefinitionManager;
	
	private HAPManagerDomainEntityDefinition m_domainEntityDefinitionManager;
	
	private HAPManagerDomainEntityExecutable m_domainEntityExecutableManager;
 	
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
			HAPParserDataExpression dataExpressionParser,
			HAPManagerScript scriptManager,
		    HAPGatewayManager gatewayManager,
		    HAPManagerService serviceManager,
		    HAPManagerDynamicResource dynamicResourceManager,
		    HAPManagerResourceDefinition resourceDefManager,
		    HAPManagerDomainEntityDefinition domainEntityManager,
			HAPManagerDomainEntityExecutable complexEntityManager,
		    HAPManagerCronJob cronJobManager,
		    HAPManagerStory storyManager,
		    HAPRuntime runtime){
		super();
		this.init(dataTypeManager, dataTypeHelper, codeTableManager, resourceMan, taskManager, activityManager, processManager, processRuntime, dataExpressionParser, scriptManager, gatewayManager, serviceManager, dynamicResourceManager, resourceDefManager, domainEntityManager, complexEntityManager, cronJobManager, storyManager, runtime);
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
				HAPParserDataExpression dataExpressionParser,
				HAPManagerScript scriptManager,
			    HAPGatewayManager gatewayManager,
			    HAPManagerService serviceManager,
			    HAPManagerDynamicResource dynamicResourceManager,
			    HAPManagerResourceDefinition resourceDefManager,
			    HAPManagerDomainEntityDefinition domainEntityDefinitionManager,
				HAPManagerDomainEntityExecutable complexEntityExecutableManager,
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
		this.m_dataExpressionParser = dataExpressionParser;
		this.m_scriptManager = scriptManager;
		this.m_serviceManager = serviceManager;
		this.m_resourceDefinitionManager = resourceDefManager;
		this.m_domainEntityDefinitionManager = domainEntityDefinitionManager;
		this.m_domainEntityExecutableManager = complexEntityExecutableManager;
		this.m_dynamicResourceManager = dynamicResourceManager;
		this.m_storyManager = storyManager;
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
		
		//task
		this.m_taskManager.registerTaskInfo(HAPConstantShared.TASK_TYPE_ACTIVITY, new HAPInfoTask(new HAPTaskInfoParserActivity(this.getActivityManager().getPluginManager()), new HAPTaskInfoProcessorActivity(this)));
		this.m_taskManager.registerTaskInfo(HAPConstantShared.TASK_TYPE_SEQUENCE, new HAPInfoTask(new HAPTaskInfoParserSequence(this.getTaskManager()), new HAPTaskInfoProcessorSequence(this.getTaskManager())));
		
		
		//component

		//resource
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, new HAPResourceManagerProcess(this.m_processManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYPLUGIN, new HAPResourceManagerActivityPlugin(this.m_activityManager.getPluginManager(), this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CRONJOB, new HAPResourceManagerCronJob(this.m_cronJobManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CODETABLE, new HAPResourceManagerCodeTable(this.m_codeTableManager, this.m_resourceManager));

		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE, new HAPResourceManagerImpConfigure(this.m_domainEntityDefinitionManager, this.m_resourceDefinitionManager, this.m_resourceManager));

		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
		
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));

		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASK, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));

//		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, new HAPResourceManagerImpScriptBased(this.m_domainEntityDefinitionManager, this.m_resourceDefinitionManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, new HAPResourceManagerImpComplex(this.m_domainEntityExecutableManager, this.m_resourceManager));
		this.m_resourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, new HAPResourceManagerImpScriptBased(this.m_domainEntityDefinitionManager, this.m_resourceDefinitionManager, this.m_resourceManager));
		

		
		//resource definition
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_1, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_COMPLEX_SCRIPT, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTTASKGROUP, this));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, this));
		
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, new HAPParserResourceValue()));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, this));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, this));

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASK, this));

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginServiceDefinition(this.getServiceManager().getServiceDefinitionManager()));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE, new HAPParserResourceDefinitionTaskSuite(this.getTaskManager())));

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpSimple(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESSSUITE, new HAPParserResourceDefinitionProcess(this.getProcessManager().getPluginManager())));
		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionProcess(this.getResourceDefinitionManager()));

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginCronJob());

		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginStory());

		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntity(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUESTRUCTURE, this));

		//domain entity definition
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTestComplex1(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTestComplexScript(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTestSimple1(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainAttachment(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainValueContext(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainValueStructure(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionDataGroup(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionDataGroupTemp(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionDataSingle(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionScriptGroup(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionScriptGroupTemp(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainExpressionScriptSingle(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainConfigure(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScript(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainDataAssociation(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainInteractive(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, HAPServiceInterface.class, this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainServiceProvider(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainDataAssociationInteractive(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainDataAssociationTask(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainData(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainValue(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainContainerComplex(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainContainerSimple(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainTask(this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScriptTaskGroup(this));

		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT, true, this));
		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, false, this));

		//complex entity processor
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexTestComplex1());
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexTestComplexScript());
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionDataGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP));
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionDataGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP));
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionDataSingle());
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionScriptGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP));
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionScriptGroup(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP));
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexExpressionScriptSingle());
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorContainerComplex());
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorContainerSimple());
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorTask());
		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorScriptTaskGroup());

		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DECORATION_SCRIPT));

		//simple entity processor
		this.getDomainEntityExecutableManager().registerSimpleEntityProcessorPlugin(new HAPPluginSimpleEntityProcessorInteractive(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE));
		this.getDomainEntityExecutableManager().registerSimpleEntityProcessorPlugin(new HAPPluginSimpleEntityProcessorServiceProvider());
		this.getDomainEntityExecutableManager().registerSimpleEntityProcessorPlugin(new HAPPluginEntityProcessorSimpleScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT));
		
		//adapter entity
		this.getDomainEntityExecutableManager().registerAdapterProcessorPlugin(new HAPPluginAdapterProcessorDataAssociation());
		this.getDomainEntityExecutableManager().registerAdapterProcessorPlugin(new HAPPluginAdapterProcessorDataAssociationInteractive());
		this.getDomainEntityExecutableManager().registerAdapterProcessorPlugin(new HAPPluginAdapterProcessorDataAssociationTask());
		
		
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
	public HAPParserDataExpression getDataExpressionParser() {    return this.m_dataExpressionParser;    }

	@Override
	public HAPManagerScript getScriptManager() {    return this.m_scriptManager;    }

	@Override
	public HAPGatewayManager getGatewayManager(){  return this.m_gatewayManager;   }

	@Override
	public HAPManagerService getServiceManager() {  return this.m_serviceManager;   }

	@Override
	public HAPManagerResourceDefinition getResourceDefinitionManager() {  return this.m_resourceDefinitionManager;  }

	@Override
	public HAPManagerDomainEntityDefinition getDomainEntityDefinitionManager() {  return this.m_domainEntityDefinitionManager;  }

	@Override
	public HAPManagerDomainEntityExecutable getDomainEntityExecutableManager() {   return this.m_domainEntityExecutableManager;   }

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
