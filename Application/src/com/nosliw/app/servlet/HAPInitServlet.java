package com.nosliw.app.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.task.expression.HAPExpressionTaskImporter;
import com.nosliw.data.expression.test.HAPExpressionTest;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.resource.HAPResourceManagerUIResource;
import com.nosliw.uiresource.resource.HAPResourceManagerUITag;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPInitServlet  extends HttpServlet{

	private static final long serialVersionUID = -703775909733982650L;

	public void init() throws ServletException
	   {
			//create runtime
			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();

//			HAPExpressionTaskImporter.importTaskDefinitionSuiteFromClassFolder(HAPExpressionTest.class, runtimeEnvironment.getExpressionManager());

			//set runtime object to context
			this.getServletContext().setAttribute("runtime", runtimeEnvironment);
			
			HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(new HAPUITagManager(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getResourceManager(), runtimeEnvironment.getRuntime(), HAPExpressionManager.dataTypeHelper);
			this.getServletContext().setAttribute("uiResourceManager", uiResourceMan);
			
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, new HAPResourceManagerUIResource(uiResourceMan));
			runtimeEnvironment.getResourceManager().registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_UITAG, new HAPResourceManagerUITag(new HAPUITagManager()));

//			String file = HAPFileUtility.getFileNameOnClassPath(HAPInitServlet.class, "Example1.res");
//			HAPUIDefinitionUnitResource uiResource = uiResourceMan.addUIResourceDefinition(file);

	   }
}
