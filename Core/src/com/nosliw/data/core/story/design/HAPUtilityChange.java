package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPUtilityChange {

	public static void reverseChangeStep(HAPStory story, HAPDesignStep step) {
		List<HAPChangeItem> changes = step.getChanges();
		revertChange(story, changes);
		reverseQuestionAnswer(story, step.getQuestionair());
	}
	
	public static void reverseQuestionAnswer(HAPStory story, HAPQuestionnaire questionair) {
		Set<HAPAnswer> answers = questionair.getAnswers();
		for(HAPAnswer answer : answers) {
			revertChange(story, answer.getChanges());
		}
		questionair.clearAnswer();
	}
	
	public static void applyChange(HAPStory story, List<HAPChangeItem> changeItems) {
		for(HAPChangeItem changeItem : changeItems) {
			applyChange(story, changeItem, null, true);
		}
	}
	
	public static void revertChange(HAPStory story, List<HAPChangeItem> changeItems) {
		//apply in revert sequence
		for(int i=changeItems.size()-1; i>=0; i--) {
			HAPChangeItem changeItem = changeItems.get(i);
			List<HAPChangeItem> revertChanges = changeItem.getRevertChanges();
			if(revertChanges!=null) {
				for(HAPChangeItem revertChange : revertChanges) {
					applyChange(story, revertChange, null, false);
				}
			}
		}
	}
	
	public static HAPStoryElement applyChange(HAPStory story, HAPChangeItem changeItem, List<HAPChangeItem> extraChanges, boolean saveRevert) {
		HAPStoryElement out = null;
		String changeType = changeItem.getChangeType();
		if(changeType.equals(HAPConstant.STORYDESIGN_CHANGETYPE_PATCH)) {
			HAPChangeItemPatch changePatch = (HAPChangeItemPatch)changeItem;
			out = story.getElement(changePatch.getTargetCategary(), changePatch.getTargetId());
			HAPChangeResult changeResult = out.patch(changePatch.getPath(), changePatch.getValue());
			//extra changes info
			if(extraChanges!=null) 	extraChanges.addAll(changeResult.getExtraChanges());
			//revert changes info
			if(saveRevert && changeItem.getRevertChanges()==null) 	changeItem.setRevertChanges(changeResult.getRevertChanges());
		}
		else if(changeType.equals(HAPConstant.STORYDESIGN_CHANGETYPE_NEW)) {
			HAPChangeItemNew changeNew = (HAPChangeItemNew)changeItem;
			HAPStoryElement element = changeNew.getElement();
			out = story.addElement(element);
			changeNew.setElement(null);
			changeNew.setTargetId(out.getId());
			//revert changes info
			if(saveRevert) {
				List<HAPChangeItem> revertChanges = new ArrayList<HAPChangeItem>();
				revertChanges.add(new HAPChangeItemDelete(out.getCategary(), out.getId()));
				changeItem.setRevertChanges(revertChanges);
			}
		}		
		else if(changeType.equals(HAPConstant.STORYDESIGN_CHANGETYPE_DELETE)) {
			HAPChangeItemDelete changeDelete = (HAPChangeItemDelete)changeItem;
			HAPStoryElement element = story.deleteElement(changeDelete.getTargetCategary(), changeDelete.getTargetId());
			//revert changes info
			if(saveRevert) {
				List<HAPChangeItem> revertChanges = new ArrayList<HAPChangeItem>();
				revertChanges.add(new HAPChangeItemNew(element));
				changeItem.setRevertChanges(revertChanges);
			}
		}
		return out;
	}
	
	public static HAPChangeInfo buildChangeNewAndApply(HAPStory story, HAPStoryElement ele, List<HAPChangeItem> changes) {
		HAPChangeItemNew change = new HAPChangeItemNew(ele);
		HAPStoryElement element = applyChangeAll(story, change, changes);
		return new HAPChangeInfo(change, element);
	}
	
	public static HAPChangeInfo buildChangeDeleteAndApply(HAPStory story, HAPIdElement elementId, List<HAPChangeItem> changes) {
		HAPChangeItemDelete change = new HAPChangeItemDelete(elementId.getCategary(), elementId.getId());
		HAPStoryElement element = applyChangeAll(story, change, changes);
		return new HAPChangeInfo(change, element);
	}
	
	public static HAPChangeInfo buildChangePatchAndApply(HAPStory story, HAPIdElement targetEleId, String path, Object value, List<HAPChangeItem> changes) {
		HAPChangeItemPatch change = new HAPChangeItemPatch(targetEleId.getCategary(), targetEleId.getId(), path, value);
		HAPStoryElement element = applyChangeAll(story, change, changes);
		return new HAPChangeInfo(change, element);
	}
	
	public static HAPChangeItem buildChangePatch(HAPStoryElement element, String path, Object value) {
		return new HAPChangeItemPatch(element.getCategary(), element.getId(), path, value);
	}
	
	private static HAPStoryElement applyChangeAll(HAPStory story, HAPChangeItem change, List<HAPChangeItem> allChanges) {
		allChanges.add(change);
		
		List<HAPChangeItem> extraChanges = new ArrayList<HAPChangeItem>();
		HAPStoryElement element = applyChange(story, change, extraChanges, true);
		
		for(HAPChangeItem extraChange : extraChanges) {
			applyChangeAll(story, extraChange, allChanges);
		}
		return element;
	}
	
	public static HAPChangeItem buildChangeNew(String itemCategary, String itemId) {
		HAPChangeItemNew out = new HAPChangeItemNew(itemCategary, itemId);
		return out;
	}
}
