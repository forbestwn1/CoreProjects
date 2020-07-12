package com.nosliw.data.core.story.design;

import com.nosliw.common.exception.HAPServiceData;

//builder that build 
public interface HAPBuilderStory {

	HAPDesignStory newDesign(String id);
	
	//design story according to change
	//   storyDesign: current design
	//   change : change
	//out: design after change
	HAPServiceData buildStory(HAPDesignStory storyDesign, HAPRequestChange changeRequest);
	
}
