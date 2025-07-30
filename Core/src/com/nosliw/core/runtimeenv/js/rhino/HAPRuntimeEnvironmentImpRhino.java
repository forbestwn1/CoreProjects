package com.nosliw.core.runtimeenv.js.rhino;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironmentJS;

//@Component
public class HAPRuntimeEnvironmentImpRhino extends HAPRuntimeEnvironmentJS{

	@HAPAttribute
	public static final String GATEWAY_SERVICE = "service";

/*	
	private HAPModuleRuntimeJS m_runtimeJSModule;
	
	public HAPRuntimeEnvironmentImpRhino(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpRhino(HAPModuleRuntimeJS runtimeJSModule) {
		this.m_runtimeJSModule = runtimeJSModule;
		
		HAPRuntime runtime = new HAPRuntimeImpRhino(this);
		HAPDataTypeManager dataTypeManager = new HAPDataTypeManagerImp(runtimeJSModule.getDataTypeDataAccess());
		HAPDataTypeHelper dataTypeHelper = new HAPDataTypeHelperImp(runtime, this.m_runtimeJSModule.getDataTypeDataAccess());
		HAPManagerCodeTable codeTableManager = new HAPManagerCodeTable();
		HAPManagerResourceJSImp resourceMan = new HAPManagerResourceJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		HAPManagerDynamicResource dynamicResourceManager = new HAPManagerDynamicResource();
		HAPManagerResourceDefinition resourceDefManager = new HAPManagerResourceDefinition(dynamicResourceManager);
		HAPManagerService serviceManager = new HAPManagerService(this);
		HAPParserDataExpression dataExpressionParser = new HAPDataExpressionParserImp();
		HAPManagerTask taskManager = new HAPManagerTask(this);
		HAPManagerActivity activityMan = new HAPManagerActivity(new HAPManagerActivityPlugin(), this);
		HAPManagerProcess processMan = new HAPManagerProcess(this);
		HAPRuntimeProcess processRuntimeMan = null;  //new HAPRuntimeProcessRhinoImp(this);
		HAPManagerCronJob cronJobManager = null;  //new HAPManagerCronJob(expressionMan, resourceMan, processMan, runtime, dataTypeHelper, serviceManager.getServiceDefinitionManager(), resourceDefManager);
		HAPManagerApplicationBrick brickManager = new HAPManagerApplicationBrick(this);
		HAPManagerUITag uiTagManager = new HAPManagerUITag(this);
		
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
				new HAPGatewayManager(),
				serviceManager,
				dynamicResourceManager,
				resourceDefManager,
				brickManager,
				uiTagManager,
				cronJobManager,
				runtime
		);

		//resource definition plugin
		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));

		//brick division
		brickManager.registerDivisionInfo(HAPConstantShared.BRICK_DIVISION_MANUAL, new HAPManualManagerBrick(this));

	}
*/	
}
