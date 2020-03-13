package com.nosliw.data.core.imp.runtime.js.browser;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.cronjob.HAPManagerCronJob;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPExpressionSuiteManager;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceManagerJSImp;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayBrowserLoadLibrary;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayLoadTestExpression;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeProcessRhinoImp;
import com.nosliw.data.core.service.provide.HAPGatewayService;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.core.template.HAPManagerTemplate;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.application.HAPResourceDefinitionPluginApp;
import com.nosliw.uiresource.application.HAPResourceDefinitionPluginAppEntry;
import com.nosliw.uiresource.module.HAPResourceDefinitionPluginModule;
import com.nosliw.uiresource.page.definition.HAPResourceDefinitionPluginPage;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

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
		
		HAPExpressionManager.dataTypeHelper = new HAPDataTypeHelperImp(this, this.m_runtimeJSModule.getDataTypeDataAccess());
		HAPExpressionManager.expressionParser = new HAPExpressionParserImp();

		HAPResourceManagerJSImp resourceMan = new HAPResourceManagerJSImp(runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess());

		HAPRuntime runtime = new HAPRuntimeImpRhino(this);
		HAPManagerTemplate templateManager = new HAPManagerTemplate();
		HAPManagerResourceDefinition resourceDefManager = new HAPManagerResourceDefinition(templateManager);
		HAPManagerService serviceManager = new HAPManagerService();
		HAPExpressionSuiteManager expSuiteMan = new HAPExpressionSuiteManager();
		HAPManagerProcess processMan = new HAPManagerProcess(new HAPManagerActivityPlugin(), resourceDefManager, HAPExpressionManager.dataTypeHelper, runtime, expSuiteMan, serviceManager.getServiceDefinitionManager());
		HAPRuntimeProcess processRuntimeMan = new HAPRuntimeProcessRhinoImp(this);
		HAPManagerCronJob cronJobManager = new HAPManagerCronJob(expSuiteMan, resourceMan, processMan, runtime, HAPExpressionManager.dataTypeHelper, serviceManager.getServiceDefinitionManager(), resourceDefManager);
		
		init(resourceMan,
			processMan,
			processRuntimeMan,
			expSuiteMan,
			new HAPGatewayManager(),
			serviceManager,
			templateManager,
			resourceDefManager,
			cronJobManager,
			runtime
		);

		this.m_uiResourceManager = new HAPUIResourceManager(
				new HAPUITagManager(),
				this.getExpressionSuiteManager(),
				this.getResourceManager(),
				this.getProcessManager(),
				this.getRuntime(),
				HAPExpressionManager.dataTypeHelper,
				this.getServiceManager().getServiceDefinitionManager(),
				this.getResourceDefinitionManager());

		//gateway
		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));
		
		this.getGatewayManager().registerGateway(GATEWAY_LOADLIBRARIES, new HAPGatewayBrowserLoadLibrary(this.getGatewayManager()));
		this.getGatewayManager().registerGateway(GATEWAY_TESTEXPRESSION, new HAPGatewayLoadTestExpression());

		//component
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginPage(this.m_uiResourceManager.getUIResourceParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginModule(this.m_uiResourceManager.getModuleParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginApp(this.m_uiResourceManager.getMinitAppParser()));
		this.getResourceDefinitionManager().registerPlugin(new HAPResourceDefinitionPluginAppEntry(this.getResourceDefinitionManager()));
	}
	
	public HAPUIResourceManager getUIResourceManager() {   return this.m_uiResourceManager;   }

}
