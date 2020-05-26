package com.nosliw.servlet.app.story;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryImp;
import com.nosliw.servlet.HAPServiceServlet;
import com.nosliw.servlet.core.HAPInitServlet;

@HAPEntityWithAttribute
public class HAPStoryBuildServlet extends HAPServiceServlet{

	@HAPAttribute
	public static final String COMMAND_NEWDESIGN = "newDesign";
	@HAPAttribute
	public static final String COMMAND_NEWDESIGN_DIRECTORID = "directorId";
	@HAPAttribute
	public static final String COMMAND_NEWDESIGN_STORYID = "storyId";
	
	@HAPAttribute
	public static final String COMMAND_DESIGN = "design";
	@HAPAttribute
	public static final String COMMAND_DESIGN_DESIGNID = "designId";
	@HAPAttribute
	public static final String COMMAND_DESIGN_CHANGE = "change";

	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) throws Exception {
		HAPServiceData out = null;
		
		HAPRuntimeEnvironmentImpBrowser env = (HAPRuntimeEnvironmentImpBrowser)this.getServletContext().getAttribute(HAPInitServlet.NAME_RUNTIME_ENVIRONMENT);
		HAPManagerStory storyManager = env.getStoryManager();
		
		switch(command){
		case COMMAND_NEWDESIGN:
		{
			HAPNewDesign design = new HAPNewDesign();
			String storyId = parms.optString(COMMAND_NEWDESIGN_STORYID);
			design.setStoryId(storyId);
			String directoryId = parms.optString(COMMAND_NEWDESIGN_STORYID);
			design.setDirector(directoryId);
			HAPStory story = null;
			if(HAPBasicUtility.isStringNotEmpty(storyId)) {
				story = storyManager.getStory(storyId);
			}
			else {
				story = new HAPStoryImp();
			}
			design.setStory(story);
			out = HAPServiceData.createSuccessData(design);
			break;
		}
		}
		return out;
	}

}
