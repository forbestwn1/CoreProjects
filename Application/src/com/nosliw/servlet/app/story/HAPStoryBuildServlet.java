package com.nosliw.servlet.app.story;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.imp.runtime.js.browser.HAPRuntimeEnvironmentImpBrowser;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.story.HAPManagerStory;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.design.HAPAnswer;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPRequestDesign;
import com.nosliw.servlet.HAPServiceServlet;
import com.nosliw.servlet.core.HAPInitServlet;

@HAPEntityWithAttribute
public class HAPStoryBuildServlet extends HAPServiceServlet{

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
	public static final String COMMAND_DESIGN_DESIGNID = "designId";
	@HAPAttribute
	public static final String COMMAND_DESIGN_ANSWER = "answer";

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
		HAPManagerStory storyManager = env.getStoryManager();
		
		switch(command){
		case COMMAND_GETDESIGN:
		{
			String designId = parms.optString(COMMAND_GETDESIGN_ID);
			HAPDesignStory design = storyManager.getStoryDesign(designId);
			out = HAPServiceData.createSuccessData(design);
			break;
		}
		case COMMAND_NEWDESIGN:
		{
			String directorId = parms.optString(COMMAND_NEWDESIGN_DIRECTORID);
			HAPDesignStory design = storyManager.newStoryDesign(directorId);
			out = HAPServiceData.createSuccessData(design);
			break;
		}
		case COMMAND_DESIGN:
		{
			String designId = parms.optString(COMMAND_DESIGN_DESIGNID);
			HAPRequestDesign changeRequest = new HAPRequestDesign(designId);

			JSONArray answerArray = parms.optJSONArray(COMMAND_DESIGN_ANSWER);
			for(int i=0; i<answerArray.length(); i++) {
				JSONObject answerObj = answerArray.getJSONObject(i);
				HAPAnswer answer = new HAPAnswer();
				answer.buildObject(answerObj, HAPSerializationFormat.JSON);
				changeRequest.addAnswer(answer);
			}
			HAPServiceData result = storyManager.designStory(designId, changeRequest);
			out = HAPServiceData.createSuccessData(result);
			break;
		}
		case COMMAND_CONVERTTOSHOW:
		{
			String designId = parms.optString(COMMAND_CONVERTTOSHOW_DESIGNID);
			HAPDesignStory design = storyManager.getStoryDesign(designId);
			HAPStory story = design.getStory();
			HAPResourceDefinition resourceDef = storyManager.buildShow(story);
			out = HAPServiceData.createSuccessData(resourceDef.getResourceId());
			break;
		}
		}
		return out;
	}

}
