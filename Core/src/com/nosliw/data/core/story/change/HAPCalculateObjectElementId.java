package com.nosliw.data.core.story.change;

import com.nosliw.common.interfac.HAPCalculateObject;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPStory;

public class HAPCalculateObjectElementId implements HAPCalculateObject{

	private HAPStory m_story;
	
	private HAPAliasElement m_alias;
	
	public HAPCalculateObjectElementId(HAPAliasElement alias, HAPStory story) {
		this.m_story = story;
		this.m_alias = alias;
	}
	
	@Override
	public Object calculate() {
		return this.m_story.getElementId(this.m_alias.getAlias());
	}

}
