package com.nosliw.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.imp.expression.HAPExpressionImporter;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;
import com.nosliw.data.expression.test.HAPExpressionTest;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.resource.HAPResourceManagerUIResource;

public class HAPInitServlet  extends HttpServlet{

	private static final long serialVersionUID = -703775909733982650L;

	public void init() throws ServletException
	   {
			//create runtime
			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();

			HAPExpressionImporter.importExpressionSuiteFromClassFolder(HAPExpressionTest.class, runtimeEnvironment.getExpressionManager());

			//set runtime object to context
			this.getServletContext().setAttribute("runtime", runtimeEnvironment);
			
			HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(runtimeEnvironment.getExpressionManager(), runtimeEnvironment.getRuntime());
			this.getServletContext().setAttribute("uiResourceManager", uiResourceMan);
			
			((HAPResourceManagerJS)runtimeEnvironment.getResourceManager()).registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, new HAPResourceManagerUIResource(uiResourceMan));

	   }
}
