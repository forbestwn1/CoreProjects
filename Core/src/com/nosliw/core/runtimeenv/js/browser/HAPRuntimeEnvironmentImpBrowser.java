package com.nosliw.core.runtimeenv.js.browser;

import org.springframework.stereotype.Component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironmentJS;

@Component
public class HAPRuntimeEnvironmentImpBrowser extends HAPRuntimeEnvironmentJS{

	@HAPAttribute
	public static final String GATEWAY_LOADLIBRARIES = "loadLibraries";

	@HAPAttribute
	public static final String GATEWAY_TESTEXPRESSION = "testExpression";
	
	@HAPAttribute
	public static final String GATEWAY_SERVICE = "service";

	@HAPAttribute
	public static final String GATEWAY_UITAG = "uiTag";

/*	
	private HAPModuleRuntimeJS m_runtimeJSModule;
	
	private HAPUIResourceManager m_uiResourceManager;
	
	public HAPRuntimeEnvironmentImpBrowser(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpBrowser(HAPModuleRuntimeJS runtimeJSModule) {
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

//		this.m_uiResourceManager = new HAPUIResourceManager(this, new HAPManagerUITag(this, dataTypeHelper));

		//brick division
		this.getBrickManager().registerDivisionInfo(HAPConstantShared.BRICK_DIVISION_MANUAL, new HAPManualManagerBrick(this));
		
		
		//gateway
		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));
		
		this.getGatewayManager().registerGateway(GATEWAY_LOADLIBRARIES, new HAPGatewayBrowserLoadLibrary(this.getGatewayManager()));
		this.getGatewayManager().registerGateway(GATEWAY_TESTEXPRESSION, new HAPGatewayLoadTestExpression());
//		this.getGatewayManager().registerGateway(GATEWAY_UITAG, new HAPGatewayUITag(this.m_uiResourceManager.getUITagManager()));

		
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainUIContent(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainUIPage(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainUITag(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainUITagDefinition(this));
//		this.getDomainEntityDefinitionManager().registerEntityDefinitionPlugin(new HAPPluginEntityDefinitionInDomainScriptBased(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, false, this));
//		
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexUIContent());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexUIPage());
//		this.getDomainEntityExecutableManager().registerComplexEntityProcessorPlugin(new HAPPluginEntityProcessorComplexUITag());

//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpEntityThin(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionUITagDefinition(this));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionUITagScript(this));

		
		
//		this.getResourceManager().registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIPAGE, new HAPResourceManagerImpComplex(this.getDomainEntityExecutableManager(), this.getResourceManager()));
//		this.getResourceManager().registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAGSCRIPT, new HAPResourceManagerImpScriptBased(this.getDomainEntityDefinitionManager(), this.getResourceDefinitionManager(), this.getResourceManager()));


		
		//resource definition plugin
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpDefault(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, this.m_uiResourceManager.getUIResourceParser()));
//		this.getResourceDefinitionManager().registerPlugin(new HAPPluginResourceDefinitionImpDefault(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE, this.m_uiResourceManager.getModuleParser()));
//		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginApp(this.m_uiResourceManager.getMinitAppParser()));
//		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginAppEntry(this.getResourceDefinitionManager()));

		
		//story builder
//		this.getStoryManager().registerDesignDirector(HAPStoryBuilderPageSimple.BUILDERID, new HAPStoryBuilderPageSimple(this.m_uiResourceManager.getUITagManager(), this));
		
		//dynamic resource builder
//		this.getStoryManager().registerShowBuilder(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, new HAPBuilderPageSimple(this.getServiceManager().getServiceDefinitionManager(), this.getUIResourceManager().getUITagManager(), this.getUIResourceManager().getUIResourceParser()));
	}
	
	public HAPUIResourceManager getUIResourceManager() {   return this.m_uiResourceManager;   }
*/	
}
