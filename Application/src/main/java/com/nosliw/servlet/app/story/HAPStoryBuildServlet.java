package com.nosliw.servlet.app.story;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.story.HAPStoryManagerStory;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryDesignStory;
import com.nosliw.core.application.division.story.design.wizard.HAPStoryRequestDesign;
import com.nosliw.core.application.division.story.design.wizard.servicedriven.HAPStoryBuilderPageSimple;
import com.nosliw.core.resource.HAPResourceDefinition1;
import com.nosliw.core.runtimeenv.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.servlet.HAPServiceServlet;
import com.nosliw.servlet.core.HAPInitServlet;

@HAPEntityWithAttribute
public class HAPStoryBuildServlet extends HAPServiceServlet{

	public static final String NAME_STORY_MANAGER = "STORYMANAGER";

	@HAPAttribute
	public static final String COMMAND_GETDESIGN = "getDesign";
	@HAPAttribute
	public static final String COMMAND_GETDESIGN_ID = "id";

	@HAPAttribute
	public static final String COMMAND_NEWDESIGN = "newDesign";
	@HAPAttribute
	public static final String COMMAND_NEWDESIGN_DIRECTORID = "directorId";
	
	@HAPAttribute
	public static final String COMMAND_DESIGN = "design";
	@HAPAttribute
	public static final String COMMAND_DESIGN_REQUEST = "request";
	@HAPAttribute
	public static final String COMMAND_DESIGN_DESIGNID = "designId";
	@HAPAttribute
	public static final String COMMAND_DESIGN_ANSWER = "answer";
	@HAPAttribute
	public static final String COMMAND_DESIGN_EXTRACHANGES = "extraChanges";
	@HAPAttribute
	public static final String COMMAND_DESIGN_STEPCUSOR = "stepCursor";

	@HAPAttribute
	public static final String COMMAND_CONVERTTOSHOW = "convertToShow";
	@HAPAttribute
	public static final String COMMAND_CONVERTTOSHOW_DESIGNID = "designId";

	@HAPAttribute
	public static final String COMMAND_SHOW = "show";
	@HAPAttribute
	public static final String COMMAND_SHOW_DESIGNID = "designId";

	@Override
	protected HAPServiceData processServiceRequest(String command, JSONObject parms) throws Exception {
		HAPServiceData out = null;
		
		HAPRuntimeEnvironmentImpBrowser env = (HAPRuntimeEnvironmentImpBrowser)this.getServletContext().getAttribute(HAPInitServlet.NAME_RUNTIME_ENVIRONMENT);
		
		HAPStoryManagerStory storyManager = (HAPStoryManagerStory)this.getServletContext().getAttribute(NAME_STORY_MANAGER);
		if(storyManager==null) {
			storyManager = new HAPStoryManagerStory(env);
			storyManager.registerDesignDirector(HAPStoryBuilderPageSimple.BUILDERID, new HAPStoryBuilderPageSimple(env.getUITagManager(), env));
			this.getServletContext().setAttribute(NAME_STORY_MANAGER, storyManager);
		}
		
		switch(command){
		case COMMAND_GETDESIGN:
		{
			String designId = parms.optString(COMMAND_GETDESIGN_ID);
			HAPStoryDesignStory design = storyManager.getStoryDesign(designId);
			out = HAPServiceData.createSuccessData(design);
			break;
		}
		case COMMAND_NEWDESIGN:
		{
			String directorId = parms.optString(COMMAND_NEWDESIGN_DIRECTORID);
			HAPStoryDesignStory design = storyManager.newStoryDesign(directorId);
			out = HAPServiceData.createSuccessData(design);
			break;
		}
		case COMMAND_DESIGN:
		{
			HAPStoryRequestDesign changeRequest = new HAPStoryRequestDesign();
			changeRequest.buildObject(parms.getJSONObject(COMMAND_DESIGN_REQUEST), HAPSerializationFormat.JSON);
			
			HAPServiceData result = storyManager.designStory(changeRequest);
			out = HAPServiceData.createSuccessData(result);
			break;
		}
		case COMMAND_CONVERTTOSHOW:
		{
			String designId = parms.optString(COMMAND_CONVERTTOSHOW_DESIGNID);
			HAPStoryDesignStory design = storyManager.getStoryDesign(designId);
			HAPStoryStory story = design.getStory();
			HAPResourceDefinition1 resourceDef = storyManager.buildShow(story);
			out = HAPServiceData.createSuccessData(resourceDef.getResourceId());
			break;
		}
		}
		return out;
	}

}
