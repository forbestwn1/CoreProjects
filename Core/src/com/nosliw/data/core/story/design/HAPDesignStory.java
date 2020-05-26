package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.story.HAPStory;

public class HAPDesignStory {

	private String m_designId;
	
	private HAPStory m_story;
	
	private List<HAPChange> m_changes;

	public HAPDesignStory(String designId) {
		this.m_designId = designId;
		this.m_changes = new ArrayList<HAPChange>();
	}
	
	public String getBuilder() {
		return null;
	}
}
