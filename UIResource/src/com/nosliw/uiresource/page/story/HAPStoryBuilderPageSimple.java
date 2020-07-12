package com.nosliw.uiresource.page.story;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPChangeBatch;
import com.nosliw.data.core.story.design.HAPChangeExtraInfoGroup;
import com.nosliw.data.core.story.design.HAPChangeExtraInfoItem;
import com.nosliw.data.core.story.design.HAPChangeItem;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPRequestChange;
import com.nosliw.data.core.story.design.HAPUtilityChange;

public class HAPStoryBuilderPageSimple implements HAPBuilderStory{

	public final static String BUILDERID = "pageSimple";
	
	@Override
	public HAPDesignStory newDesign(String id) {
		HAPDesignStory design = new HAPDesignStory(id);
		
		HAPStoryNodeImp serviceNode = new HAPStoryNodeImp(HAPConstant.STORYNODE_TYPE_SERVICE, design.getNextId());
		design.getStory().addNode(serviceNode);
		
		HAPChangeBatch changeBatch = new HAPChangeBatch();
		HAPChangeItem changeItem = HAPUtilityChange.buildChangeNew(serviceNode.getId());
		changeBatch.addChangeItem(changeItem);
		
		HAPChangeExtraInfoGroup groupExtraInfo = new HAPChangeExtraInfoGroup();
		HAPChangeExtraInfoItem serviceItemExtraInfo = new HAPChangeExtraInfoItem(serviceNode.getId());
		groupExtraInfo.addItem(serviceItemExtraInfo);
		changeBatch.setExtraInfo(groupExtraInfo);
		
		return design;
	}

	@Override
	public HAPServiceData buildStory(HAPDesignStory storyDesign, HAPRequestChange changeRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private HAPChangeBatch processServiceChange(HAPStory story, HAPChangeItem changeItem) {
		//discover dependency
//		List<HAPStoryElement> eles = HAPUtilityStory.getAllElementRelyOnIt(story, eleId);
		
		//remove all dependency
		
		
		//build all 
		
		return null;
	}



}
