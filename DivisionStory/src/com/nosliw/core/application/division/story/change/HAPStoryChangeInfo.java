package com.nosliw.core.application.division.story.change;

import com.nosliw.core.application.division.story.brick.HAPStoryElement;

public class HAPStoryChangeInfo {

	private HAPStoryElement m_element;
	
	private HAPStoryChangeItem m_changeItem;
	
	public HAPStoryChangeInfo(HAPStoryChangeItem changeItem, HAPStoryElement element) {
		this.m_element = element;
		this.m_changeItem = changeItem;
	}
	
	public HAPStoryElement getStoryElement() {    return this.m_element;   }
	public HAPStoryChangeItem getChangeItem() {   return this.m_changeItem;    }
	
}
