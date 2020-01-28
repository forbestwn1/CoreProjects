package com.nosliw.data.core.imp.runtime.js.browser;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.strvalue.valueinfo.HAPValueInfoManager;
import com.nosliw.data.core.component.HAPManagerComponent;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.imp.HAPDataTypeHelperImp;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.resource.HAPResourceManagerJSImp;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.runtime.HAPGatewayManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.HAPRuntimeEnvironmentJS;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayBrowserLoadLibrary;
import com.nosliw.data.core.runtime.js.browser.HAPGatewayLoadTestExpression;
import com.nosliw.data.core.runtime.js.rhino.HAPManagerProcessImp;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.service.provide.HAPGatewayService;
import com.nosliw.data.core.service.provide.HAPManagerService;
import com.nosliw.data.imp.expression.parser.HAPExpressionParserImp;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.definition.HAPComponentPluginPage;
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

		HAPRuntime runtime = new HAPRuntimeImpRhino(this);
		HAPManagerService serviceManager = new HAPManagerService();
		HAPExpressionSuiteManager expSuiteMan = new HAPExpressionSuiteManager();
		HAPManagerProcessDefinition processDefMan = new HAPManagerProcessDefinition(new HAPManagerActivityPlugin(), HAPExpressionManager.dataTypeHelper, runtime, expSuiteMan, serviceManager.getServiceDefinitionManager());
		HAPManagerProcess processMan = new HAPManagerProcessImp(processDefMan, runtime);
		HAPManagerComponent componentManager = new HAPManagerComponent();
		
		init(new HAPResourceManagerJSImp(
				runtimeJSModule.getRuntimeJSDataAccess(), runtimeJSModule.getDataTypeDataAccess()),
				processDefMan,
				processMan,
				expSuiteMan,
			new HAPGatewayManager(),
			serviceManager,
			componentManager,
			runtime
		);

		this.m_uiResourceManager = new HAPUIResourceManager(
				new HAPUITagManager(),
				this.getExpressionSuiteManager(),
				this.getResourceManager(),
				this.getProcessDefinitionManager(),
				this.getRuntime(),
				HAPExpressionManager.dataTypeHelper,
				this.getServiceManager().getServiceDefinitionManager(),
				this.getComponentManager());

		//gateway
		this.getGatewayManager().registerGateway(GATEWAY_SERVICE, new HAPGatewayService(this.getServiceManager()));
		
		this.getGatewayManager().registerGateway(GATEWAY_LOADLIBRARIES, new HAPGatewayBrowserLoadLibrary(this.getGatewayManager()));
		this.getGatewayManager().registerGateway(GATEWAY_TESTEXPRESSION, new HAPGatewayLoadTestExpression());

		//component
		this.getComponentManager().registerPlugin(new HAPComponentPluginPage(this.m_uiResourceManager.getUIResourceParser()));
	}
	
	public HAPUIResourceManager getUIResourceManager() {   return this.m_uiResourceManager;   }

}
