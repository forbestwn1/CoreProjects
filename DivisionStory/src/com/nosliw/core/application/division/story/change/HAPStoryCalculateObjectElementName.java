package com.nosliw.core.application.division.story.change;

import com.nosliw.common.interfac.HAPCalculateObject;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryStory;

public class HAPStoryCalculateObjectElementName implements HAPCalculateObject{

	private HAPStoryStory m_story;
	
	private HAPStoryAliasElement m_alias;
	
	public HAPStoryCalculateObjectElementName(HAPStoryAliasElement alias, HAPStoryStory story) {
		this.m_story = story;
		this.m_alias = alias;
	}
	
	@Override
	public Object calculate() {
		return this.m_story.getElement(m_alias).getName();
	}
}
