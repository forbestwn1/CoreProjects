package com.nosliw.data.core.imp.runtime.js.browser;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
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
import com.nosliw.data.core.template.HAPManagerTemplate;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.tag.HAPUITagManager;
import com.nosliw.uiresource.page.template.HAPBuilderPageSimple;
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
	
	private HAPModuleRuntimeJS m_runtimeJSModule;
	
	private HAPUIResourceManager m_uiResourceManager;
	
	public HAPRuntimeEnvironmentImpBrowser(){
		this(new HAPModuleRuntimeJS().init(HAPValueInfoManager.getInstance()));
	}
	
	public HAPRuntimeEnvironmentImpBrowser(HAPModuleRuntimeJS runtimeJSModule) {
		this.m_runtimeJSModule = runtimeJSModule;
		
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
		HAPManagerTemplate templateManager = new HAPManagerTemplate(resourceDefManager); 
		
		init(
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
			templateManager,
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

		//resource definition plugin
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginPage(this.m_uiResourceManager.getUIResourceParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginModule(this.m_uiResourceManager.getModuleParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginApp(this.m_uiResourceManager.getMinitAppParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginAppEntry(this.getResourceDefinitionManager()));

		//dynamic resource builder
		this.getDynamicResourceManager().registerDynamicResourceBuilder("page_minimum", new HAPBuilderPageSimple(this.getServiceManager().getServiceDefinitionManager(), this.getUIResourceManager().getUITagManager(), this.getUIResourceManager().getUIResourceParser()));
	}
	
	public HAPUIResourceManager getUIResourceManager() {   return this.m_uiResourceManager;   }

}
