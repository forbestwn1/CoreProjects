package com.nosliw.servlet.core;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.imp.io.HAPDBSource;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
//import com.nosliw.miniapp.HAPAppManager;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.resource.HAPResourceManagerUIAppConfigure;
import com.nosliw.uiresource.resource.HAPResourceManagerUIAppEntry;
import com.nosliw.uiresource.resource.HAPResourceManagerUIModule;
import com.nosliw.uiresource.resource.HAPResourceManagerUIModuleDecoration;
import com.nosliw.uiresource.resource.HAPResourceManagerUIResource;
import com.nosliw.uiresource.resource.HAPResourceManagerUITag;

public class HAPInitServlet  extends HttpServlet{

	private static final long serialVersionUID = -703775909733982650L;

	public static final String NAME_RUNTIME_ENVIRONMENT = "RUNTIME_ENVIRONMENT";
	
	@Override
	public void init() throws ServletException
	   {
			//create runtime
			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();

//			HAPExpressionTaskImporter.importTaskDefinitionSuiteFromClassFolder(HAPExpressionTest.class, runtimeEnvironment.getExpressionManager());

//			HAPUIResourceManager uiResourceMan = HAPRuntimeUtility.geHUIResourceManager(runtimeEnvironment);
			HAPUIResourceManager uiResourceMan = runtimeEnvironment.getUIResourceManager();
			this.getServletContext().setAttribute("uiResourceManager", uiResourceMan);
			
			HAPResourceManagerRoot rootResourceManager = runtimeEnvironment.getResourceManager(); 
			rootResourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, new HAPResourceManagerUIResource(uiResourceMan, rootResourceManager));
			rootResourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, new HAPResourceManagerUITag(runtimeEnvironment.getUIResourceManager().getUITagManager(), rootResourceManager));
			rootResourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE, new HAPResourceManagerUIModule(uiResourceMan, rootResourceManager));
			rootResourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULEDECORATION, new HAPResourceManagerUIModuleDecoration(rootResourceManager));
			rootResourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPPENTRY, new HAPResourceManagerUIAppEntry(uiResourceMan, rootResourceManager));
			rootResourceManager.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIAPPCONFIGURE, new HAPResourceManagerUIAppConfigure(rootResourceManager));

			runtimeEnvironment.getGatewayManager().registerGateway(HAPConstantShared.GATEWAY_OPTIONS, new HAPGatewayOptions());
			
			//set runtime object to context
			this.getServletContext().setAttribute(NAME_RUNTIME_ENVIRONMENT, runtimeEnvironment);
			
//			HAPAppManager appManager = new HAPAppManager();
//			this.getServletContext().setAttribute("minAppMan", appManager);
//			runtimeEnvironment.getGatewayManager().registerGateway(HAPGatewayAppData.GATEWAY_APPDATA, new HAPGatewayAppData(appManager.getAppDataManager()));
	   }
	
	@Override
    public void destroy() {
		HAPDBSource.getDefaultDBSource().destroy();
    }
}
