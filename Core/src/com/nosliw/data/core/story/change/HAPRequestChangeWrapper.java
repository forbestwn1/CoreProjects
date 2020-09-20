package com.nosliw.data.core.story.change;

import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPRequestChangeWrapper {

	private HAPStory m_story;
	
	private HAPRequestChange m_changeRequest;
	
	public HAPRequestChangeWrapper(HAPStory story) {
		this.m_story = story;
		m_changeRequest = new HAPRequestChange();
	}
	
	public HAPAliasElement addNewChange(HAPStoryElement storyElement) {
		HAPAliasElement out = this.m_story.generateTemporaryAlias();
		m_changeRequest.addChange(new HAPChangeItemNew(storyElement, out));
		return out;
	}
	
	public HAPAliasElement addNewChange(HAPStoryElement storyElement, String alias) {
		HAPAliasElement out = new HAPAliasElement(alias, false);
		m_changeRequest.addChange(new HAPChangeItemNew(storyElement, out));
		return out;
	}
	
	public void addPatchChange(HAPReferenceElement targetRef, String path, Object value){
		HAPChangeItemPatch change = new HAPChangeItemPatch(targetRef, path, value);
		this.m_changeRequest.addChange(change);
	}
	
	public HAPRequestChange getChangeRequest() {    return this.m_changeRequest;     }
}
