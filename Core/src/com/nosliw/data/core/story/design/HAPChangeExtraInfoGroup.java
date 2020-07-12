package com.nosliw.data.core.story.design;

import java.util.List;

public class HAPChangeExtraInfoGroup extends HAPChangeExtra{

	List<HAPChangeExtra> m_items;

	public void addItem(HAPChangeExtra item) {
		this.m_items.add(item);
	}
	
}
