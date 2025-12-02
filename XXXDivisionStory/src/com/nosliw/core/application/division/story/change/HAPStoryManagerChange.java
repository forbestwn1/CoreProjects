package com.nosliw.core.application.division.story.change;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.interfac.HAPCalculateObject;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryManagerChange {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPStoryManagerChange(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public void revertChange(HAPStoryStory story, List<HAPStoryChangeItem> changeItems) {
		//apply in revert sequence
		for(int i=changeItems.size()-1; i>=0; i--) {
			HAPStoryChangeItem changeItem = changeItems.get(i);
			List<HAPStoryChangeItem> revertChanges = changeItem.getRevertChanges();
			if(revertChanges!=null) {
				for(HAPStoryChangeItem revertChange : revertChanges) {
					applySingleChange(story, revertChange, null, false);
				}
			}
		}
	}

	//apply change, not triggured extend changes to story
	public void applyChanges(HAPStoryStory story, List<HAPStoryChangeItem> changeItems) {		
		for(HAPStoryChangeItem change : changeItems) {
			applyChange(story, change);
		}
	}

	//apply change and triggued extend changes to story
	//allChanges : story all changes
	public void applyChanges(HAPStoryStory story, List<HAPStoryChangeItem> changeItems, List<HAPStoryChangeItem> allChanges) {
		for(HAPStoryChangeItem change : changeItems) {
			applyChange(story, change, allChanges);
		}
	}
	
	//apply change, not triggured extend changes to story
	public void applyChange(HAPStoryStory story, HAPStoryChangeItem changeItem) {		applySingleChange(story, changeItem, null, true);	}
	
	//apply change and triggued extend changes to story
	//allChanges : story all changes
	public HAPStoryElement applyChange(HAPStoryStory story, HAPStoryChangeItem change, List<HAPStoryChangeItem> allChanges) {
		allChanges.add(change);
		
		List<HAPStoryChangeItem> extendChanges = new ArrayList<HAPStoryChangeItem>();
		HAPStoryElement element = applySingleChange(story, change, extendChanges, true);
		
		for(HAPStoryChangeItem extendChange : extendChanges) {
			applyChange(story, extendChange, allChanges);
		}
		return element;
	}	

	//apply change to story
	//     extendedChanges :  sometimes, one change may trigue more extend changes
	//     saveRevert : whether save revert information when apply change.
	//return story element related with change
	private HAPStoryElement applySingleChange(HAPStoryStory story, HAPStoryChangeItem changeItem, List<HAPStoryChangeItem> extendChanges, boolean saveRevert) {
		HAPStoryElement out = null;
		String changeType = changeItem.getChangeType();
		if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_PATCH)) {
			HAPStoryChangeItemPatch changePatch = (HAPStoryChangeItemPatch)changeItem;
			changePatch.processAlias(story);
			out = story.getElement(changePatch.getTargetElementId());
			Object patchObj = changePatch.getValue();
			if(patchObj instanceof HAPCalculateObject) {
				changePatch.setValue(((HAPCalculateObject)patchObj).calculate());
			}
			HAPStoryChangeResult changeResult = out.patch(changePatch.getPath(), changePatch.getValue(), this.m_runtimeEnv);
			//extend changes info
			if(extendChanges!=null && !changeItem.isExtended()) {
				for(HAPStoryChangeItem extendItem : changeResult.getExtendChanges()) {
					extendItem.setId(story.getNextId());
					extendItem.setExtendFrom(changeItem.getId());
					extendChanges.add(extendItem);
				}
			}
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				changeItem.setRevertChanges(changeResult.getRevertChanges());
			}
		}
		else if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_NEW)) {
			HAPStoryChangeItemNew changeNew = (HAPStoryChangeItemNew)changeItem;
			HAPStoryElement element = changeNew.getElement();
			HAPStoryAliasElement alias = changeNew.getAlias();
			HAPStoryIdElement oldAliasEleId = null;
			if(alias!=null)   oldAliasEleId = story.getElementId(alias.getName());
			out = story.addElement(element, alias);
			changeNew.setElement(out.cloneStoryElement());
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPStoryChangeItem> revertChanges = new ArrayList<HAPStoryChangeItem>();
				revertChanges.add(new HAPStoryChangeItemDelete(out.getElementId()));
				if(alias!=null && !alias.isTemporary()) {
					if(oldAliasEleId!=null) revertChanges.add(new HAPStoryChangeItemAlias(alias, oldAliasEleId));
				}
				changeItem.setRevertChanges(revertChanges);
			}
		}		
		else if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE)) {
			HAPStoryChangeItemDelete changeDelete = (HAPStoryChangeItemDelete)changeItem;
			changeDelete.processAlias(story);
			HAPStoryAliasElement alias = story.getAlias(changeDelete.getTargetElementId());
			HAPStoryElement element = story.deleteElement(changeDelete.getTargetElementId());
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPStoryChangeItem> revertChanges = new ArrayList<HAPStoryChangeItem>();
				revertChanges.add(new HAPStoryChangeItemNew(element, alias));
				changeItem.setRevertChanges(revertChanges);
			}
		}
		else if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_ALIAS)) {
			HAPStoryChangeItemAlias changeAlias = (HAPStoryChangeItemAlias)changeItem;
			HAPStoryIdElement oldElementId = story.setAlias(changeAlias.getAlias(), changeAlias.getElementId());
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPStoryChangeItem> revertChanges = new ArrayList<HAPStoryChangeItem>();
				revertChanges.add(new HAPStoryChangeItemAlias(changeAlias.getAlias(), oldElementId));
				changeItem.setRevertChanges(revertChanges);
			}
		}
		else if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_STORYINFO)) {
			HAPStoryChangeItemStoryInfo changeStoryIndex = (HAPStoryChangeItemStoryInfo)changeItem;
			String infoName = changeStoryIndex.getInfoName();
			Object infoValue = changeStoryIndex.getInfoValue();
			HAPInfo storyInfo = story.getInfo();
			Object oldValue = storyInfo.getValue(infoName);
			storyInfo.setValue(infoName, infoValue);
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPStoryChangeItem> revertChanges = new ArrayList<HAPStoryChangeItem>();
				revertChanges.add(new HAPStoryChangeItemStoryInfo(infoName, oldValue));
				changeItem.setRevertChanges(revertChanges);
			}
		}
		if(extendChanges!=null)  changeItem.setExtended();
		return out;
	}
	
	private boolean ifRevertable(boolean saveRevert, HAPStoryChangeItem changeItem) {
		if(!saveRevert)  return false;
		return changeItem.isRevertable();
	}

}
