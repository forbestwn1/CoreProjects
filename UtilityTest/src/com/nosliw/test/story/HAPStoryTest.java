package com.nosliw.test.story;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.division.story.HAPStoryManagerStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryDesignStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryRequestDesign;
import com.nosliw.core.application.division.story.design.wizard.servicedriven.HAPStoryBuilderPageSimple;
import com.nosliw.core.xxx.runtimeenv.js.browser.HAPRuntimeEnvironmentImpBrowser;

public class HAPStoryTest {

	public static void main(String[] args) {

		try {
			String id = "page_minimum";
			String parmSet = "testData1";

			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
			
			HAPStoryManagerStory storyMan = new HAPStoryManagerStory(runtimeEnvironment);
			storyMan.registerDesignDirector(HAPStoryBuilderPageSimple.BUILDERID, new HAPStoryBuilderPageSimple(runtimeEnvironment.getUITagManager(), runtimeEnvironment));

			//new design
			String designId = "0000";
			HAPStoryDesignStory design = storyMan.newStoryDesign(HAPStoryBuilderPageSimple.BUILDERID, designId);

			//service selected
			HAPStoryRequestDesign changeRequest = new HAPStoryRequestDesign();
			changeRequest.buildObject(new JSONObject(HAPUtilityFile.readFile(HAPStoryTest.class, "designreqest_selectservice.json")), HAPSerializationFormat.JSON);
			changeRequest.setDesignId(designId);
			storyMan.designStory(changeRequest);
			
			
			HAPStoryDesignStory designRead = storyMan.getStoryDesign(design.getId());

//			HAPStoryStory story = design.getStory();
//			HAPResourceDefinition1 resourceDefinition = storyMan.buildShow(story);
			
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
