package com.nosliw.test.story;

import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionStory;

public class HAPStoryTest {

	public static void main(String[] args) {

		try {
			String id = "page_minimum";
			String parmSet = "testData1";

			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
			
			HAPResourceDefinitionStory story = runtimeEnvironment.getStoryManager().getStoryResource(id);
			HAPResourceDefinition resourceDefinition = runtimeEnvironment.getStoryManager().buildShow(story);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
