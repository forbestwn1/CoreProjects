package com.nosliw.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.miniapp.HAPAppManager;
import com.nosliw.miniapp.HAPGatewayAppData;
//import com.nosliw.miniapp.HAPAppManager;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.tag.HAPUITagManager;
import com.nosliw.uiresource.resource.HAPResourceManagerUIAppConfigure;
import com.nosliw.uiresource.resource.HAPResourceManagerUIAppEntry;
import com.nosliw.uiresource.resource.HAPResourceManagerUIModule;
import com.nosliw.uiresource.resource.HAPResourceManagerUIModuleDecoration;
import com.nosliw.uiresource.resource.HAPResourceManagerUIResource;
import com.nosliw.uiresource.resource.HAPResourceManagerUITag;

public class HAPInitServlet  extends HttpServlet{

	private static final long serialVersionUID = -703775909733982650L;

	@Override
	public void init() throws ServletException
	   {
			//create runtime
			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();

//			HAPExpressionTaskImporter.importTaskDefinitionSuiteFromClassFolder(HAPExpressionTest.class, runtimeEnvironment.getExpressionManager());

			//set runtime object to context
			this.getServletContext().setAttribute("runtime", runtimeEnvironment);
			
			HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(
					new HAPUITagManager(),
					runtimeEnvironment.getExpressionSuiteManager(),
					runtimeEnvironment.getResourceManager(),
					runtimeEnvironment.getProcessManager(),
					runtimeEnvironment.getRuntime(),
					HAPExpressionManager.dataTypeHelper,
					runtimeEnvironment.getServiceManager().getServiceDefinitionManager());
			this.getServletContext().setAttribute("uiResourceManager", uiResourceMan);
			
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, new HAPResourceManagerUIResource(uiResourceMan));
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UITAG, new HAPResourceManagerUITag(new HAPUITagManager()));
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE, new HAPResourceManagerUIModule(uiResourceMan));
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION, new HAPResourceManagerUIModuleDecoration());
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPENTRY, new HAPResourceManagerUIAppEntry(uiResourceMan));
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UIAPPCONFIGURE, new HAPResourceManagerUIAppConfigure());

			runtimeEnvironment.getGatewayManager().registerGateway(HAPConstant.GATEWAY_OPTIONS, new HAPGatewayOptions());
			
			HAPAppManager appManager = new HAPAppManager(uiResourceMan);
			this.getServletContext().setAttribute("minAppMan", appManager);
			runtimeEnvironment.getGatewayManager().registerGateway(HAPGatewayAppData.GATEWAY_APPDATA, new HAPGatewayAppData(appManager));
	   }
}
