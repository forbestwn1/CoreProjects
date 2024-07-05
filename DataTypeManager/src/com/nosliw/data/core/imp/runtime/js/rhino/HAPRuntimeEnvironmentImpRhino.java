package com.nosliw.data.core.imp.runtime.js.rhino;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.service.HAPGatewayService;
import com.nosliw.core.application.service.HAPManagerService;
import com.nosliw.data.core.activity.HAPManagerActivity;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.codetable.HAPManagerCodeTable;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPManagerResourceJSImp;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPRuntimeProcess;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.script.expression1.HAPManagerScript;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.imp.expression.parser.HAPDataExpressionParserImp;

public class HAPRuntimeEnvironmentImpRhino extends HAPRuntimeEnvironmentJS{

	@HAPAttribute
	public static final String GATEWAY_SERVICE = "service";

	private HAPModuleRuntimeJS m_runtimeJSModule;
	
	public HAPRuntimeEnvironmentImpRhino(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpRhino(HAPModuleRuntimeJS runtimeJSModule) {
		this.m_runtimeJSModule = runtimeJSModule;
		
		HAPDataTypeManager dataTypeManager = new HAPDataTypeManagerImp(runtimeJSModule.getDataTypeDataAccess());
		HAPDataTypeHelper dataTypeHelper = new HAPDataTypeHelperImp(this, this.m_runtimeJSModule.getDataTypeDataAccess());
		HAPManagerCodeTable codeTableManager = new HAPManagerCodeTable();
		HAPRuntime runtime = new HAPRuntimeImpRhino(this);
		HAPManagerResourceJSImp resourceMan = new HAPManagerResourceJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		HAPManagerDynamicResource dynamicResourceManager = new HAPManagerDynamicResource();
		HAPManagerResourceDefinition resourceDefManager = new HAPManagerResourceDefinition(dynamicResourceManager);
		HAPManagerService serviceManager = new HAPManagerService(this);
		HAPParserDataExpression dataExpressionParser = new HAPDataExpressionParserImp();
		HAPManagerScript scriptMan = new HAPManagerScript(this);
		HAPManagerTask taskManager = new HAPManagerTask(this);
		HAPManagerActivity activityMan = new HAPManagerActivity(new HAPManagerActivityPlugin(), this);
		HAPManagerProcess processMan = new HAPManagerProcess(this);
		HAPRuntimeProcess processRuntimeMan = null;  //new HAPRuntimeProcessRhinoImp(this);
		HAPManagerCronJob cronJobManager = null;  //new HAPManagerCronJob(expressionMan, resourceMan, processMan, runtime, dataTypeHelper, serviceManager.getServiceDefinitionManager(), resourceDefManager);
		HAPManagerStory storyManager = new HAPManagerStory(this); 
		HAPManagerApplicationBrick brickManager = new HAPManagerApplicationBrick(this);
		
		init(
				dataTypeManager,
				dataTypeHelper,
				codeTableManager,
				resourceMan,
				taskManager,
				activityMan,
				processMan,
				processRuntimeMan,
				dataExpressionParser,
				scriptMan,
				new HAPGatewayManager(),
				serviceManager,
				dynamicResourceManager,
				resourceDefManager,
				brickManager,
				cronJobManager,
				storyManager,
				runtime
		);

		//resource definition plugin
		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));

		//brick division
		brickManager.registerDivisionInfo(HAPConstantShared.BRICK_DIVISION_MANUAL, new HAPManualManagerBrick(this));

	}
}
