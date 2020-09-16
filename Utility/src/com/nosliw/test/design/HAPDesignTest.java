package com.nosliw.test.design;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPUtilityChange;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPRequestDesign;
import com.nosliw.uiresource.page.story.design.HAPStoryBuilderPageSimple;

public class HAPDesignTest {

	public static void main(String[] args) {

		try {
			String id = "page_minimum";
			String parmSet = "testData1";

			HAPRuntimeEnvironmentImpBrowser runtimeEnvironment = new HAPRuntimeEnvironmentImpBrowser();
			HAPManagerStory storyMan = runtimeEnvironment.getStoryManager();
			
			HAPDesignStory design = storyMan.newStoryDesign(HAPStoryBuilderPageSimple.BUILDERID);
			String designId = design.getId();
			storyMan.saveStoryDesign(design);
			
			HAPRequestDesign changeRequest = new HAPRequestDesign(designId);
			Map<String, Object> value = new LinkedHashMap<String, Object>();
			value.put("entity.service.referenceId", "TestTemplateService");
			HAPChangeItem changeItem = HAPUtilityChange.buildItemPatch("", value);
			changeRequest.addChangeItem(changeItem);
			HAPServiceData serviceData = storyMan.designStory(designId, changeRequest);
			System.out.println(serviceData);
		}
		catch(Throwable e) {
			e.printStackTrace();
		}
	}
}
