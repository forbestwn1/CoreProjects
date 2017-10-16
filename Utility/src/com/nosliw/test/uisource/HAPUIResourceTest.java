package com.nosliw.test.uisource;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.uiresource.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.HAPUIResource;
import com.nosliw.uiresource.HAPUIResourceManager;

public class HAPUIResourceTest {

	public static void main(String[] agrs){

		//module init
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		//start runtime
		runtimeEnvironment.getRuntime().start();

		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(runtimeEnvironment.getExpressionManager(), runtimeEnvironment.getRuntime());

		String file = HAPFileUtility.getFileNameOnClassPath(HAPUIResourceTest.class, "Example.res");
		HAPUIDefinitionUnitResource uiResource = uiResourceMan.addUIResourceDefinition(file);
		HAPUIResource resource = uiResourceMan.processUIResource("Example", null);
		System.out.println(resource);
		
	}
	
}
