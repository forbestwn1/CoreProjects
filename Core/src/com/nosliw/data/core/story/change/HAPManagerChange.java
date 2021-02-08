package com.nosliw.data.core.story.change;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.interfac.HAPCalculateObject;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPManagerChange {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerChange(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public void revertChange(HAPStory story, List<HAPChangeItem> changeItems) {
		//apply in revert sequence
		for(int i=changeItems.size()-1; i>=0; i--) {
			HAPChangeItem changeItem = changeItems.get(i);
			List<HAPChangeItem> revertChanges = changeItem.getRevertChanges();
			if(revertChanges!=null) {
				for(HAPChangeItem revertChange : revertChanges) {
					applySingleChange(story, revertChange, null, false);
				}
			}
		}
	}

	//apply change, not triggured extend changes to story
	public void applyChanges(HAPStory story, List<HAPChangeItem> changeItems) {		
		for(HAPChangeItem change : changeItems) {
			applyChange(story, change);
		}
	}

	//apply change and triggued extend changes to story
	//allChanges : story all changes
	public void applyChanges(HAPStory story, List<HAPChangeItem> changeItems, List<HAPChangeItem> allChanges) {
		for(HAPChangeItem change : changeItems) {
			applyChange(story, change, allChanges);
		}
	}
	
	//apply change, not triggured extend changes to story
	public void applyChange(HAPStory story, HAPChangeItem changeItem) {		applySingleChange(story, changeItem, null, true);	}
	
	//apply change and triggued extend changes to story
	//allChanges : story all changes
	public HAPStoryElement applyChange(HAPStory story, HAPChangeItem change, List<HAPChangeItem> allChanges) {
		allChanges.add(change);
		
		List<HAPChangeItem> extendChanges = new ArrayList<HAPChangeItem>();
		HAPStoryElement element = applySingleChange(story, change, extendChanges, true);
		
		for(HAPChangeItem extendChange : extendChanges) {
			applyChange(story, extendChange, allChanges);
		}
		return element;
	}	

	//apply change to story
	//     extendedChanges :  sometimes, one change may trigue more extend changes
	//     saveRevert : whether save revert information when apply change.
	//return story element related with change
	private HAPStoryElement applySingleChange(HAPStory story, HAPChangeItem changeItem, List<HAPChangeItem> extendChanges, boolean saveRevert) {
		HAPStoryElement out = null;
		String changeType = changeItem.getChangeType();
		if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_PATCH)) {
			HAPChangeItemPatch changePatch = (HAPChangeItemPatch)changeItem;
			changePatch.processAlias(story);
			out = story.getElement(changePatch.getTargetElementId());
			Object patchObj = changePatch.getValue();
			if(patchObj instanceof HAPCalculateObject) {
				changePatch.setValue(((HAPCalculateObject)patchObj).calculate());
			}
			HAPChangeResult changeResult = out.patch(changePatch.getPath(), changePatch.getValue(), this.m_runtimeEnv);
			//extend changes info
			if(extendChanges!=null && !changeItem.isExtended()) {
				for(HAPChangeItem extendItem : changeResult.getExtendChanges()) {
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
			HAPChangeItemNew changeNew = (HAPChangeItemNew)changeItem;
			HAPStoryElement element = changeNew.getElement();
			HAPAliasElement alias = changeNew.getAlias();
			HAPIdElement oldAliasEleId = null;
			if(alias!=null)   oldAliasEleId = story.getElementId(alias.getName());
			out = story.addElement(element, alias);
			changeNew.setElement(out.cloneStoryElement());
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPChangeItem> revertChanges = new ArrayList<HAPChangeItem>();
				revertChanges.add(new HAPChangeItemDelete(out.getElementId()));
				if(alias!=null && !alias.isTemporary()) {
					if(oldAliasEleId!=null) revertChanges.add(new HAPChangeItemAlias(alias, oldAliasEleId));
				}
				changeItem.setRevertChanges(revertChanges);
			}
		}		
		else if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE)) {
			HAPChangeItemDelete changeDelete = (HAPChangeItemDelete)changeItem;
			changeDelete.processAlias(story);
			HAPAliasElement alias = story.getAlias(changeDelete.getTargetElementId());
			HAPStoryElement element = story.deleteElement(changeDelete.getTargetElementId());
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPChangeItem> revertChanges = new ArrayList<HAPChangeItem>();
				revertChanges.add(new HAPChangeItemNew(element, alias));
				changeItem.setRevertChanges(revertChanges);
			}
		}
		else if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_ALIAS)) {
			HAPChangeItemAlias changeAlias = (HAPChangeItemAlias)changeItem;
			HAPIdElement oldElementId = story.setAlias(changeAlias.getAlias(), changeAlias.getElementId());
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPChangeItem> revertChanges = new ArrayList<HAPChangeItem>();
				revertChanges.add(new HAPChangeItemAlias(changeAlias.getAlias(), oldElementId));
				changeItem.setRevertChanges(revertChanges);
			}
		}
		else if(changeType.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_STORYINFO)) {
			HAPChangeItemStoryInfo changeStoryIndex = (HAPChangeItemStoryInfo)changeItem;
			String infoName = changeStoryIndex.getInfoName();
			Object infoValue = changeStoryIndex.getInfoValue();
			HAPInfo storyInfo = story.getInfo();
			Object oldValue = storyInfo.getValue(infoName);
			storyInfo.setValue(infoName, infoValue);
			//revert changes info
			if(ifRevertable(saveRevert, changeItem)) {
				List<HAPChangeItem> revertChanges = new ArrayList<HAPChangeItem>();
				revertChanges.add(new HAPChangeItemStoryInfo(infoName, oldValue));
				changeItem.setRevertChanges(revertChanges);
			}
		}
		if(extendChanges!=null)  changeItem.setExtended();
		return out;
	}
	
	private boolean ifRevertable(boolean saveRevert, HAPChangeItem changeItem) {
		if(!saveRevert)  return false;
		return changeItem.isRevertable();
	}

}
