package com.nosliw.test.template;

import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.template.HAPOutputBuilder;
import com.nosliw.data.core.template.resource.HAPResourceDefinitionTemplate;

public class HAPTemplateTest {

	public static void main(String[] args) {

		try {
			String id = "page_minimum";
			String parmSet = "testData1";

			HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
			
			HAPResourceDefinitionTemplate template = runtimeEnvironment.getTemplateManager().getTemplate(id);
			HAPOutputBuilder builderOutput = runtimeEnvironment.getTemplateManager().tryBuildResource(template.getBuilderId(), template.getParmsDef(parmSet));
			
			System.out.println(builderOutput);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
