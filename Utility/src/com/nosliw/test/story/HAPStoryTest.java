package com.nosliw.test.story;

import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.design.HAPDesignStory;

public class HAPStoryTest {

	public static void main(String[] args) {

		try {
			String id = "page_minimum";
			String parmSet = "testData1";

			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
			
			HAPDesignStory design = runtimeEnvironment.getStoryManager().getStoryDesign("1596913798816");
			HAPStory story = design.getStory();
			HAPResourceDefinition1 resourceDefinition = runtimeEnvironment.getStoryManager().buildShow(story);
			
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
