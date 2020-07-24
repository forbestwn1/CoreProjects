package com.nosliw.data.core.story.design;

import com.nosliw.data.core.story.HAPStoryElement;

public class HAPChangeResult {

	private HAPStoryElement m_element;
	
	private HAPChangeItem m_changeItem;
	
	public HAPChangeResult(HAPChangeItem changeItem, HAPStoryElement element) {
		this.m_element = element;
		this.m_changeItem = changeItem;
	}
	
	public HAPStoryElement getStoryElement() {    return this.m_element;   }
	public HAPChangeItem getChangeItem() {   return this.m_changeItem;    }
	
}
