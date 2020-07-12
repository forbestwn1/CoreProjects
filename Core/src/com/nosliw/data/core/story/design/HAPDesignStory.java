package com.nosliw.data.core.story.design;

import java.util.List;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryImp;

public class HAPDesignStory extends HAPEntityInfoImp{

	private HAPStory m_story;
	
	private List<HAPChangeBatch> m_changeHistory;
	
	public HAPDesignStory(String designId) {
		this.setId(designId);
		this.m_story = new HAPStoryImp();
	}
	
	public String getBuilder() {
		return null;
	}

	public HAPStory getStory() {
		return this.m_story;
	}
	
	public String getNextId() {
		
	}
}
