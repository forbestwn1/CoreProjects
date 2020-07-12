package com.nosliw.data.core.story.design;

import java.util.List;

public class HAPChangeBatch {

	private List<HAPChangeItem> m_changeRequst;
	
	private List<HAPChangeItem> m_changeItems;

	private HAPChangeExtraInfoGroup m_group;

	public void addChangeItem(HAPChangeItem changeItem) {	this.m_changeItems.add(changeItem);	}
	
	public void setExtraInfo(HAPChangeExtraInfoGroup group) {
		this.m_group = group;
	}
	
}
