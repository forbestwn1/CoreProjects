package com.nosliw.data.core.story.design;

import java.util.List;

public class HAPRequestChange {

	private List<HAPChangeItem> m_changes;

	private String m_designId;
	
	public HAPRequestChange(String buildId) {
		
	}
	
	public String getDesignId() {   return this.m_designId;   }
	
	public void addChangeItem(HAPChangeItem changeItem) {  this.m_changes.add(changeItem);   }
}
