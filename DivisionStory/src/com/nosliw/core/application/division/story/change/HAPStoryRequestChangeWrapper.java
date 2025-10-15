package com.nosliw.core.application.division.story.change;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryInfoElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryUtilityStory;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.xxx.brick.HAPStoryElementGroup;

public class HAPStoryRequestChangeWrapper {

	private HAPStoryStory m_story;
	
	//current request
	private HAPStoryRequestChange m_changeRequest;
	
	private boolean m_isAutoApply = false;
	
	private Boolean m_extend;
	
	//store all changes
	private List<HAPStoryChangeItem> m_allChanages;
	
	public HAPStoryRequestChangeWrapper(HAPStoryStory story) {
		this(story, true, null);
	}
	
	public HAPStoryRequestChangeWrapper(HAPStoryStory story, boolean isAutoApply, Boolean extend) {
		this.m_allChanages = new ArrayList<HAPStoryChangeItem>();
		this.m_story = story;
		this.m_isAutoApply = isAutoApply;
		this.m_extend = extend;
	}
	
	public List<HAPStoryChangeItem> close() {
		applyCurrentChangeRequests();
		return this.m_allChanages;
	}

	public HAPStoryStory getStory() {  return this.m_story;    }
	
	public void addChange(HAPStoryChangeItem changeItem) {	this.processChange(changeItem);	}
	
	public void addChanges(List<HAPStoryChangeItem> changeItems) {
		for(HAPStoryChangeItem changeItem : changeItems) {
			this.addChange(changeItem);
		}
	}

	public HAPStoryChangeItemNew addNewChange(HAPStoryElement storyElement) {
		return this.addNewChange(storyElement, null);
	}
	
	public HAPStoryChangeItemNew addNewChange(HAPStoryElement storyElement, String alias) {
		if(HAPUtilityBasic.isStringEmpty(storyElement.getId())) {
			storyElement.setId(HAPStoryUtilityStory.buildStoryElementId(storyElement, this.m_story.getNextId()));
		}
		HAPStoryAliasElement aliasObj = null;
		if(alias!=null) {
			aliasObj = new HAPStoryAliasElement(alias, false);
		} else {
			aliasObj = this.m_story.generateTemporaryAlias();
		}
		HAPStoryChangeItemNew out = new HAPStoryChangeItemNew(storyElement, aliasObj);
		this.processChange(out);
		return out;
	}
	
	public HAPStoryChangeItemPatch addPatchChange(HAPStoryReferenceElement targetRef, String path, Object value){
		HAPStoryChangeItemPatch change = new HAPStoryChangeItemPatch(targetRef, path, value);
		this.processChange(change);
		return change;
	}
	
	public HAPStoryChangeItemPatch addPatchChangeGroupAppendElement(HAPStoryReferenceElement targetReference, HAPStoryInfoElement eleInfo) {
		eleInfo.setId(this.m_story.getNextId());
		HAPStoryChangeItemPatch change = new HAPStoryChangeItemPatch(targetReference, HAPStoryElementGroup.ELEMENT, eleInfo);
		this.processChange(change);
		return change;
	}
	
	private void processChange(HAPStoryChangeItem changeItem) {
		if(this.m_isAutoApply) {
			HAPStoryRequestChange changeRequest = newChangeRequest(); 
			changeRequest.addChange(changeItem);
			this.applyChangeRequest(changeRequest);
		}
		else {
			if(this.m_changeRequest==null) {
				this.m_changeRequest = new HAPStoryRequestChange(this.m_story);
			}
			this.m_changeRequest.addChange(changeItem);
		}
	}
	
	private HAPStoryRequestChange newChangeRequest() {	return this.m_story.newRequestChange(this.m_extend);	}
	
	private void applyChangeRequest(HAPStoryRequestChange changeRequest) {
		List<HAPStoryChangeItem> allChanges = this.m_story.change(changeRequest);
		if(allChanges!=null) {
			this.m_allChanages.addAll(allChanges);
		}
	}

	private void applyCurrentChangeRequests() {
		if(this.m_changeRequest!=null) {
			this.applyChangeRequest(this.m_changeRequest);
			this.m_changeRequest = null;
		}
	}
	
}
