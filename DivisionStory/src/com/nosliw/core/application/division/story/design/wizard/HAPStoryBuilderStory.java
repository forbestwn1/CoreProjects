package com.nosliw.core.application.division.story.design.wizard;

import com.nosliw.common.exception.HAPServiceData;

//builder that build 
public interface HAPStoryBuilderStory {

	void initDesign(HAPStoryDesignStory design);
	
	//design story according to change
	//   storyDesign: current design
	//   change : change
	//out: design after change
	HAPServiceData buildStory(HAPStoryDesignStory storyDesign, HAPStoryRequestDesign changeRequest);
	
}
