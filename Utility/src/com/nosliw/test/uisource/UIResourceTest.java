package com.nosliw.test.uisource;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.runtime.js.HAPRuntimeEnvironmentImpJS;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUIResourceParser;

public class UIResourceTest {

	public static void main(String[] agrs){

		//module init
		HAPRuntimeEnvironmentImpJS runtimeEnvironment = new HAPRuntimeEnvironmentImpJS();
		
		//start runtime
		runtimeEnvironment.getRuntime().start();
		
		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(runtimeEnvironment.getExpressionManager(), runtimeEnvironment.getRuntime());

		String file = HAPFileUtility.getFileNameOnClassPath(HAPUIResourceParser.class, "Example.res");
		uiResourceMan.processUIResource(file);
	}
	
}
