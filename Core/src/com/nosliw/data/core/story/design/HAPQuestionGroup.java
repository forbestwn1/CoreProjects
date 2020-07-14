package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;

public class HAPQuestionGroup extends HAPQuestion{

	private List<HAPQuestion> m_items;

	public HAPQuestionGroup() {
		this.m_items = new ArrayList<HAPQuestion>();
	}
	
	public void addItem(HAPQuestion item) {
		this.m_items.add(item);
	}
	
}
