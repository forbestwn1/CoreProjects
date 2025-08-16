package com.nosliw.test.uisource;

import java.io.FileNotFoundException;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.xxx.runtimeenv.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.resource.HAPResourceIdUIModule;

public class HAPModuleMain {

	static public void main(String[] args) throws FileNotFoundException {

		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
		
		HAPExecutableModule pageExe = runtimeEnvironment.getUIResourceManager().getUIModule(new HAPResourceIdUIModule("verify"));
		System.out.println(HAPUtilityJson.formatJson(pageExe.toStringValue(HAPSerializationFormat.JSON)));
//		System.out.println(pageExe.toResourceData(runtimeEnvironment.getRuntime().getRuntimeInfo()));
		
//		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(new HAPUITagManager(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getResourceManager(), runtimeEnvironment.getRuntime(), HAPExpressionManager.dataTypeHelper);

//		String file = HAPFileUtility.getFileNameOnClassPath(HAPUIResourceTest.class, "Example1.res");
//		uiResourceMan.addUIResourceDefinition(file);
//		HAPExecutableUIUnitResource resource = uiResourceMan.getUIResource("Example1");
//		System.out.println(resource);

		
		/*
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(
				new HAPUITagManager(), 
				runtimeEnvironment.getExpressionSuiteManager(), 
				runtimeEnvironment.getResourceManager(),
				runtimeEnvironment.getProcessManager(),
				runtimeEnvironment.getRuntime(), 
				HAPExpressionManager.dataTypeHelper
			);
		
//		HAPDefinitionModule module = HAPUtilityUIResource.getUIModuleDefinitionById("ModuleMySchoolResult", uiResourceMan.getModuleParser());
		
		uiResourceMan.getUIModule("ModuleMySchoolResult");
		
		*/
	}
	
}
