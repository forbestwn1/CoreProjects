package com.nosliw.core.application.division.story.change;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.division.story.HAPStoryStory;

public class HAPStoryRequestChange {

	private HAPStoryStory m_story;
	
	private List<HAPStoryChangeItem> m_changes;
	
	private Boolean m_extend;
	
	public HAPStoryRequestChange(HAPStoryStory story) {
		this(null, story);
	}

	public HAPStoryRequestChange(Boolean extend, HAPStoryStory story) {
		this.m_story = story;
		this.m_changes = new ArrayList<HAPStoryChangeItem>();
		this.m_extend = extend;
		if(this.m_extend==null)   this.m_extend = true;
	}

	public List<HAPStoryChangeItem> getChanges(){   return this.m_changes;    }
	public void addChange(HAPStoryChangeItem change) {
		if(HAPUtilityBasic.isStringEmpty(change.getId())) {
			change.setId(this.m_story.getNextId());
		}
		this.m_changes.add(change);     
	}
	public void addChanges(List<HAPStoryChangeItem> changes) {
		for(HAPStoryChangeItem change : changes) {
			this.addChange(change);
		}
	}
	
	public boolean isExtend() {   return this.m_extend;    }
	public void notExtend() {   this.m_extend = false;    }
	
}
