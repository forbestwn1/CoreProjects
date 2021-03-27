package com.nosliw.test.uisource;

import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;
import com.nosliw.uiresource.resource.HAPResourceIdUIResource;

public class HAPUIResourceTest {

	public static void main(String[] agrs){

		//module init
		HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
		
//		HAPDefinitionUIPage pageDef = runtimeEnvironment.getUIResourceManager().getUIPageDefinition(new HAPResourceIdUIResource("test"), null);
		
		HAPExecutableUIUnitPage pageExe = runtimeEnvironment.getUIResourceManager().getUIPage(new HAPResourceIdUIResource("mytest"));
//		System.out.println(pageExe.toStringValue(HAPSerializationFormat.JSON));
		System.out.println(pageExe.toResourceData(runtimeEnvironment.getRuntime().getRuntimeInfo()));
		
//		HAPUIResourceManager uiResourceMan = new HAPUIResourceManager(new HAPUITagManager(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getResourceManager(), runtimeEnvironment.getRuntime(), HAPExpressionManager.dataTypeHelper);

//		String file = HAPFileUtility.getFileNameOnClassPath(HAPUIResourceTest.class, "Example1.res");
//		uiResourceMan.addUIResourceDefinition(file);
//		HAPExecutableUIUnitResource resource = uiResourceMan.getUIResource("Example1");
//		System.out.println(resource);
		
	}
	
}
