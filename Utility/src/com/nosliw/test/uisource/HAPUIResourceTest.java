package com.nosliw.test.uisource;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPUIResourceTest {

	public static void main(String[] agrs){

		//module init
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(new HAPUITagManager(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getResourceManager(), runtimeEnvironment.getRuntime(), HAPExpressionManager.dataTypeHelper);

		String file = HAPFileUtility.getFileNameOnClassPath(HAPUIResourceTest.class, "Example1.res");
		uiResourceMan.addUIResourceDefinition(file);
		HAPUIDefinitionUnitResource resource = uiResourceMan.getUIResource("Example1");
		System.out.println(resource);
		
	}
	
}
