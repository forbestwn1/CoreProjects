package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;

public class HAPRequestChange {

	private List<HAPChangeItem> m_changes;

	private String m_designId;
	
	public HAPRequestChange(String buildId) {
		this.m_changes = new ArrayList<HAPChangeItem>();
	}
	
	public String getDesignId() {   return this.m_designId;   }
	
	public void addChangeItem(HAPChangeItem changeItem) {  this.m_changes.add(changeItem);   }

	public List<HAPChangeItem> getChangeItems(){    return this.m_changes;    }
}
