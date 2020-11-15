package com.nosliw.data.core.imp.runtime.js.browser;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.HAPDataTypeManager;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.HAPDataTypeManagerImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceManagerJSImp;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.resource.dynamic.HAPManagerDynamicResource;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayBrowserLoadLibrary;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayLoadTestExpression;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeProcessRhinoImp;
import com.nosliw.data.core.script.expression.HAPManagerScript;
import com.nosliw.data.core.service.provide.HAPGatewayService;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.story.design.HAPBuilderPageSimple;
import com.nosliw.uiresource.page.story.design.builder.service.HAPStoryBuilderPageSimple;
import com.nosliw.uiresource.page.tag.HAPGatewayUITag;
import com.nosliw.uiresource.page.tag.HAPUITagManager;
import com.nosliw.uiresource.resource.HAPResourceDefinitionPluginApp;
import com.nosliw.uiresource.resource.HAPResourceDefinitionPluginAppEntry;
import com.nosliw.uiresource.resource.HAPResourceDefinitionPluginModule;
import com.nosliw.uiresource.resource.HAPResourceDefinitionPluginPage;

public class HAPRuntimeEnvironmentImpBrowser extends HAPRuntimeEnvironmentJS{

	@HAPAttribute
	public static final String GATEWAY_LOADLIBRARIES = "loadLibraries";

	@HAPAttribute
	public static final String GATEWAY_TESTEXPRESSION = "testExpression";
	
	@HAPAttribute
	public static final String GATEWAY_SERVICE = "service";

	@HAPAttribute
	public static final String GATEWAY_UITAG = "uiTag";
	
	private HAPModuleRuntimeJS m_runtimeJSModule;
	
	private HAPUIResourceManager m_uiResourceManager;
	
	public HAPRuntimeEnvironmentImpBrowser(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpBrowser(HAPModuleRuntimeJS runtimeJSModule) {
		this.m_runtimeJSModule = runtimeJSModule;
		
		HAPDataTypeManager dataTypeManager = new HAPDataTypeManagerImp(runtimeJSModule.getDataTypeDataAccess());
		HAPDataTypeHelper dataTypeHelper = new HAPDataTypeHelperImp(this, this.m_runtimeJSModule.getDataTypeDataAccess());
		HAPResourceManagerJSImp resourceMan = new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());
		HAPRuntime runtime = new HAPRuntimeImpRhino(this);
		HAPManagerDynamicResource dynamicResourceManager = new HAPManagerDynamicResource();
		HAPManagerResourceDefinition resourceDefManager = new HAPManagerResourceDefinition(dynamicResourceManager);
		HAPManagerService serviceManager = new HAPManagerService();
		HAPManagerExpression expressionMan = new HAPManagerExpression(new HAPExpressionParserImp(), resourceDefManager, dataTypeHelper, runtime, serviceManager.getServiceDefinitionManager());
		HAPManagerScript scriptMan = new HAPManagerScript(expressionMan, resourceDefManager, dataTypeHelper, runtime, serviceManager.getServiceDefinitionManager());
		HAPManagerProcess processMan = new HAPManagerProcess(new HAPManagerActivityPlugin(), resourceDefManager, dataTypeHelper, runtime, expressionMan, serviceManager.getServiceDefinitionManager());
		HAPRuntimeProcess processRuntimeMan = new HAPRuntimeProcessRhinoImp(this);
		HAPManagerCronJob cronJobManager = new HAPManagerCronJob(expressionMan, resourceMan, processMan, runtime, dataTypeHelper, serviceManager.getServiceDefinitionManager(), resourceDefManager);
		HAPManagerStory storyManager = new HAPManagerStory(resourceDefManager); 
		
		init(
			dataTypeManager,
			dataTypeHelper,
			resourceMan,
			processMan,
			processRuntimeMan,
			expressionMan,
			scriptMan,
			new HAPGatewayManager(),
			serviceManager,
			dynamicResourceManager,
			resourceDefManager,
			cronJobManager,
			storyManager,
			runtime
		);

		this.m_uiResourceManager = new HAPUIResourceManager(
				new HAPUITagManager(dataTypeHelper),
				this.getExpressionManager(),
				this.getResourceManager(),
				this.getProcessManager(),
				this.getRuntime(),
				dataTypeHelper,
				this.getServiceManager().getServiceDefinitionManager(),
				this.getResourceDefinitionManager());

		//gateway
		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));
		
		this.getGatewayManager().registerGateway(GATEWAY_LOADLIBRARIES, new HAPGatewayBrowserLoadLibrary(this.getGatewayManager()));
		this.getGatewayManager().registerGateway(GATEWAY_TESTEXPRESSION, new HAPGatewayLoadTestExpression());
		this.getGatewayManager().registerGateway(GATEWAY_UITAG, new HAPGatewayUITag(this.m_uiResourceManager.getUITagManager()));

		//resource definition plugin
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginPage(this.m_uiResourceManager.getUIResourceParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginModule(this.m_uiResourceManager.getModuleParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginApp(this.m_uiResourceManager.getMinitAppParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginAppEntry(this.getResourceDefinitionManager()));

		//story builder
		this.getStoryManager().registerDesignDirector(HAPStoryBuilderPageSimple.BUILDERID, new HAPStoryBuilderPageSimple(this.getServiceManager(), this.m_uiResourceManager.getUITagManager(), this.getResourceDefinitionManager(), dataTypeHelper, this.getRuntime(), this.getExpressionManager(), this.getServiceManager().getServiceDefinitionManager(), this.getDataTypeManager()));
		
		//dynamic resource builder
		this.getStoryManager().registerShowBuilder(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, new HAPBuilderPageSimple(this.getServiceManager().getServiceDefinitionManager(), this.getUIResourceManager().getUITagManager(), this.getUIResourceManager().getUIResourceParser()));
	}
	
	public HAPUIResourceManager getUIResourceManager() {   return this.m_uiResourceManager;   }
}
