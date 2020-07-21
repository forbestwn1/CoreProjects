package com.nosliw.uiresource.page.story;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPChangeBatch;
import com.nosliw.data.core.story.design.HAPChangeItem;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPQuestionGroup;
import com.nosliw.data.core.story.design.HAPQuestionItem;
import com.nosliw.data.core.story.design.HAPRequestChange;
import com.nosliw.data.core.story.design.HAPUtilityChange;
import com.nosliw.data.core.story.design.HAPUtilityDesign;

public class HAPStoryBuilderPageSimple implements HAPBuilderStory{

	public final static String BUILDERID = "pageSimple";
	
	public final static String STAGE_SERVICE = "service";
	public final static String STAGE_UI = "ui";
	public final static String STAGE_END = "end";
	
	@Override
	public void initDesign(HAPDesignStory design) {
		design.getStory().setShowType(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE);
		
		HAPStoryNodeImp serviceNode = new HAPStoryNodeImp(HAPConstant.STORYNODE_TYPE_SERVICE, design.getNextId());
		design.getStory().addNode(serviceNode);
		
		HAPChangeBatch changeBatch = new HAPChangeBatch();
		
		//new service node
		HAPChangeItem changeItem = HAPUtilityChange.buildChangeNew(HAPConstant.STORYELEMENT_CATEGARY_NODE, serviceNode.getId());
		changeBatch.addChange(changeItem);
		
		//extra info
		HAPQuestionGroup groupExtraInfo = new HAPQuestionGroup("Please select service.");
		HAPQuestionItem serviceItemExtraInfo = new HAPQuestionItem("select service", HAPConstant.STORYELEMENT_CATEGARY_NODE, serviceNode.getId());
		groupExtraInfo.addChild(serviceItemExtraInfo);
		changeBatch.setQuestion(groupExtraInfo);
		
		//stage
		HAPUtilityDesign.setChangeStage(changeBatch, STAGE_SERVICE);

		design.addChangeBatch(changeBatch);
	}

	@Override
	public HAPServiceData buildStory(HAPDesignStory storyDesign, HAPRequestChange changeRequest) {
		HAPServiceData out = null;
		String stage = HAPUtilityDesign.getDesignStage(storyDesign);
		if(stage.equals(STAGE_SERVICE)) {
			out = this.processServiceStage(storyDesign, changeRequest);
		}
		else if(stage.equals(STAGE_UI)) {
			out = this.processUIChangeStage(storyDesign, changeRequest);
		}
		else if(stage.equals(STAGE_END)) {
			
		}
		
		return out;
	}
	
	private HAPServiceData processServiceStage(HAPDesignStory design, HAPRequestChange changeRequest) {
		//discover dependency
//		List<HAPStoryElement> eles = HAPUtilityStory.getAllElementRelyOnIt(story, eleId);
		
		//remove all dependency
		
		
		//build all 
		
		return null;
	}

	private HAPServiceData processUIChangeStage(HAPDesignStory design, HAPRequestChange changeRequest) {
		return null;
	}


}
