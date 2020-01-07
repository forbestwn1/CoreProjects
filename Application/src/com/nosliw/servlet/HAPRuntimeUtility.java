package com.nosliw.servlet;

import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPRuntimeUtility {

	public static HAPUIResourceManager geHUIResourceManager(HAPRuntimeEnvironmentImpBrowser runtimeEnvironment) {
		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(
				new HAPUITagManager(),
				runtimeEnvironment.getExpressionSuiteManager(),
				runtimeEnvironment.getResourceManager(),
				runtimeEnvironment.getProcessDefinitionManager(),
				runtimeEnvironment.getRuntime(),
				HAPExpressionManager.dataTypeHelper,
				runtimeEnvironment.getServiceManager().getServiceDefinitionManager());
		return uiResourceMan;
	}
	
}
