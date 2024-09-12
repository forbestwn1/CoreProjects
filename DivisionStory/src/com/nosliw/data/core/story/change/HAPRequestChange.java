package com.nosliw.data.core.story.change;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.data.core.story.HAPStory;

public class HAPRequestChange {

	private HAPStory m_story;
	
	private List<HAPChangeItem> m_changes;
	
	private Boolean m_extend;
	
	public HAPRequestChange(HAPStory story) {
		this(null, story);
	}

	public HAPRequestChange(Boolean extend, HAPStory story) {
		this.m_story = story;
		this.m_changes = new ArrayList<HAPChangeItem>();
		this.m_extend = extend;
		if(this.m_extend==null)   this.m_extend = true;
	}

	public List<HAPChangeItem> getChanges(){   return this.m_changes;    }
	public void addChange(HAPChangeItem change) {
		if(HAPUtilityBasic.isStringEmpty(change.getId())) {
			change.setId(this.m_story.getNextId());
		}
		this.m_changes.add(change);     
	}
	public void addChanges(List<HAPChangeItem> changes) {
		for(HAPChangeItem change : changes) {
			this.addChange(change);
		}
	}
	
	public boolean isExtend() {   return this.m_extend;    }
	public void notExtend() {   this.m_extend = false;    }
	
}
