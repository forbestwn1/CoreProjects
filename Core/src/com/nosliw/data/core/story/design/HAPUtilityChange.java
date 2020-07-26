package com.nosliw.data.core.story.design;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPUtilityChange {

	public static void applyChange(HAPStory story, List<HAPChangeItem> changeItems) {
		for(HAPChangeItem changeItem : changeItems) {
			applyChange(story, changeItem, null);
		}
	}
	
	public static HAPStoryElement applyChange(HAPStory story, HAPChangeItem changeItem, List<HAPChangeItem> extraChanges) {
		HAPStoryElement out = null;
		String changeType = changeItem.getChangeType();
		if(changeType.equals(HAPConstant.STORYDESIGN_CHANGETYPE_PATCH)) {
			HAPChangeItemPatch changePatch = (HAPChangeItemPatch)changeItem;
			out = story.getElement(changePatch.getTargetCategary(), changePatch.getTargetId());
			List<HAPChangeItem> changes = out.patch(changePatch.getPath(), changePatch.getValue());
			if(extraChanges!=null) {
				extraChanges.addAll(changes);
			}
		}
		return out;
	}
	
	public static HAPChangeResult buildChangeNewAndApply(HAPStory story, HAPStoryElement ele, List<HAPChangeItem> changes) {
		HAPStoryElement ele1 = story.addElement(ele);
		HAPChangeItemNew change = new HAPChangeItemNew(ele1.getCategary(), ele1.getId());
		changes.add(change);
		return new HAPChangeResult(change, ele1);
	}
	
	public static HAPChangeResult buildChangePatchAndApply(HAPStory story, HAPIdElement targetEleId, String path, Object value, List<HAPChangeItem> changes) {
		HAPChangeItemPatch change = new HAPChangeItemPatch(targetEleId.getCategary(), targetEleId.getId(), path, value);
		HAPStoryElement element = applyChangeAll(story, change, changes);
		return new HAPChangeResult(change, element);
	}
	
	private static HAPStoryElement applyChangeAll(HAPStory story, HAPChangeItem change, List<HAPChangeItem> allChanges) {
		allChanges.add(change);
		
		List<HAPChangeItem> extraChanges = new ArrayList<HAPChangeItem>();
		HAPStoryElement element = applyChange(story, change, extraChanges);
		
		for(HAPChangeItem extraChange : extraChanges) {
			applyChangeAll(story, extraChange, allChanges);
		}
		return element;
	}
	

	public static HAPChangeItem buildChangeNew(String itemCategary, String itemId) {
		HAPChangeItemNew out = new HAPChangeItemNew(itemCategary, itemId);
		return out;
		
	}

	public static HAPChangeItem buildItemPatch(String itemId, Map<String, Object> values) {
		return null;
	}
	


//	public static HAPStory applyReverseChange(HAPStory story, HAPChangeBatch change) {
//		
//	}
//	
//	public static HAPChangeItem buildItemDelete(List<HAPIdStoryElement> elementIds) {
//		
//	}
//	
//	public static HAPChangeItem buildItemPut() {
//		
//	}
	
}
