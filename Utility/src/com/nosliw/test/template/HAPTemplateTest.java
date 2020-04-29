package com.nosliw.test.template;

import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.dynamic.HAPOutputBuilder;
import com.nosliw.data.core.template.HAPResourceDefinitionTemplate;

public class HAPTemplateTest {

	public static void main(String[] args) {

		try {
			String id = "page_minimum";
			String parmSet = "testData1";

			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
			
			HAPResourceDefinitionTemplate template = runtimeEnvironment.getTemplateManager().getTemplate(id);
			HAPOutputBuilder builderOutput = runtimeEnvironment.getDynamicResourceManager().tryBuildResource(template.getBuilderId(), template.getParmsDef(parmSet));
			
			System.out.println(builderOutput);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
