package com.nosliw.data.core.story.change;

import java.util.List;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPElementGroup;
import com.nosliw.data.core.story.HAPInfoElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPUtilityStory;

public class HAPRequestChangeWrapper {

	private HAPStory m_story;
	
	private HAPRequestChange m_changeRequest;
	
	private boolean m_isAutoApply = false;
	
	private Boolean m_extend;
	
	public HAPRequestChangeWrapper(HAPStory story) {
		this(story, false, null);
	}
	
	public HAPRequestChangeWrapper(HAPStory story, boolean isAutoApply, Boolean extend) {
		this.m_story = story;
		this.m_isAutoApply = isAutoApply;
		this.m_extend = extend;
		if(!this.m_isAutoApply) {
			m_changeRequest = newChangeRequest();
		}
	}
	
	public void addChange(HAPChangeItem changeItem) {	this.processRequest(changeItem);	}
	
	public void addChanges(List<HAPChangeItem> changeItems) {
		for(HAPChangeItem changeItem : changeItems)  this.addChange(changeItem);
	}

	public HAPChangeItemNew addNewChange(HAPStoryElement storyElement) {
		return this.addNewChange(storyElement, null);
	}
	
	public HAPChangeItemNew addNewChange(HAPStoryElement storyElement, String alias) {
		if(HAPBasicUtility.isStringEmpty(storyElement.getId())) storyElement.setId(HAPUtilityStory.buildStoryElementId(storyElement, this.m_story.getNextId()));
		HAPAliasElement aliasObj = null;
		if(alias!=null)		aliasObj = new HAPAliasElement(alias, false);
		else aliasObj = this.m_story.generateTemporaryAlias();
		HAPChangeItemNew out = new HAPChangeItemNew(storyElement, aliasObj);
		this.processRequest(out);
		return out;
	}
	
	public HAPChangeItemPatch addPatchChange(HAPReferenceElement targetRef, String path, Object value){
		HAPChangeItemPatch change = new HAPChangeItemPatch(targetRef, path, value);
		this.processRequest(change);
		return change;
	}
	
	public HAPChangeItemPatch addPatchChangeGroupAppendElement(HAPReferenceElement targetReference, HAPInfoElement eleInfo) {
		eleInfo.setId(this.m_story.getNextId());
		HAPChangeItemPatch change = new HAPChangeItemPatch(targetReference, HAPElementGroup.ELEMENT, eleInfo);
		this.processRequest(change);
		return change;
	}
	
	private void processRequest(HAPChangeItem changeItem) {
		if(this.m_isAutoApply) {
			HAPRequestChange changeRequest = newChangeRequest(); 
			changeRequest.addChange(changeItem);
			this.m_story.change(changeRequest);
		}
		else {
			this.m_changeRequest.addChange(changeItem);
		}
	}
	
	private HAPRequestChange newChangeRequest() {	return this.m_story.newRequestChange(this.m_extend);	}
	
	public List<HAPChangeItem> close() {
		List<HAPChangeItem> out = null;
		if(this.m_changeRequest!=null) {
			out = this.m_story.change(m_changeRequest);
			this.m_changeRequest = null;
		}
		return out;
	}
}
