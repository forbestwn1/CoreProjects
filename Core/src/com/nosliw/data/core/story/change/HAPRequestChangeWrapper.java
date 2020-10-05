package com.nosliw.data.core.story.change;

import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPRequestChangeWrapper {

	private HAPStory m_story;
	
	private HAPRequestChange m_changeRequest;
	
	private boolean m_isAutoApply = false;
	
	public HAPRequestChangeWrapper(HAPStory story) {
		this(story, false);
	}
	
	public HAPRequestChangeWrapper(HAPStory story, boolean isAutoApply) {
		this.m_story = story;
		this.m_isAutoApply = isAutoApply;
		if(!this.m_isAutoApply) {
			m_changeRequest = new HAPRequestChange();
		}
	}

	public HAPAliasElement addNewChange(HAPStoryElement storyElement) {
		HAPAliasElement out = this.m_story.generateTemporaryAlias();
		this.processRequest(new HAPChangeItemNew(storyElement, out));
		return out;
	}
	
	public HAPAliasElement addNewChange(HAPStoryElement storyElement, String alias) {
		HAPAliasElement out = new HAPAliasElement(alias, false);
		this.processRequest(new HAPChangeItemNew(storyElement, out));
		return out;
	}
	
	public void addPatchChange(HAPReferenceElement targetRef, String path, Object value){
		HAPChangeItemPatch change = new HAPChangeItemPatch(targetRef, path, value);
		this.processRequest(change);
	}
	
	private void processRequest(HAPChangeItem changeItem) {
		if(this.m_isAutoApply) {
			HAPRequestChange changeRequest = new HAPRequestChange();
			changeRequest.addChange(changeItem);
			this.m_story.change(changeRequest);
		}
		else {
			this.m_changeRequest.addChange(changeItem);
		}
	}
	
	public void close() {
		if(this.m_changeRequest!=null) {
			this.m_story.change(m_changeRequest);
			this.m_changeRequest = null;
		}
	}
}
