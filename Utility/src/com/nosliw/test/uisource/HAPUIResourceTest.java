package com.nosliw.test.uisource;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.runtime.js.HAPRuntimeEnvironmentImpJS;
import com.nosliw.uiresource.HAPUIResource;
import com.nosliw.uiresource.HAPUIResourceManager;

public class HAPUIResourceTest {

	public static void main(String[] agrs){

		//module init
		HAPRuntimeEnvironmentImpJS runtimeEnvironment = new HAPRuntimeEnvironmentImpJS();
		
		//start runtime
		runtimeEnvironment.getRuntime().start();

		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(runtimeEnvironment.getExpressionManager(), runtimeEnvironment.getRuntime());

		String file = HAPFileUtility.getFileNameOnClassPath(HAPUIResourceTest.class, "Example.res");
		HAPUIResource uiResource = uiResourceMan.addUIResource(file);
		System.out.println(uiResource);
		
	}
	
}
