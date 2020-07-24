package com.nosliw.data.core.story.design;

import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPUtilityChange {

	public static void applyChange(HAPStory story, List<HAPChangeItem> changeItems) {
		for(HAPChangeItem changeItem : changeItems) {
			applyChange(story, changeItem);
		}
	}
	
	public static HAPStoryElement applyChange(HAPStory story, HAPChangeItem changeItem) {
		HAPStoryElement out = null;
		String changeType = changeItem.getChangeType();
		if(changeType.equals(HAPConstant.STORYDESIGN_CHANGETYPE_PATCH)) {
			HAPChangeItemPatch changePatch = (HAPChangeItemPatch)changeItem;
			out = story.getElement(changePatch.getTargetCategary(), changePatch.getTargetId());
			out.patch(changePatch.getPath(), changePatch.getValue());
		}
		return out;
	}
	
	public static HAPChangeResult buildChangeNewAndApply(HAPStory story, HAPStoryElement ele, List<HAPChangeItem> changes) {
		HAPStoryElement ele1 = story.addElement(ele);
		HAPChangeItemNew change = new HAPChangeItemNew(ele1.getCategary(), ele1.getId());
		changes.add(change);
		return new HAPChangeResult(change, ele1);
	}
	
	public static HAPChangeResult buildChangePatchAndApply(HAPStory story, String targetCategary, String targetId, String path, String value, List<HAPChangeItem> changes) {
		HAPChangeItemPatch change = new HAPChangeItemPatch(targetCategary, targetId, path, value);
		HAPStoryElement element = applyChange(story, change);
		changes.add(change);
		return new HAPChangeResult(change, element);
	}
	

	public static HAPStory applyChange(HAPStory story, HAPDesignStep change) {
	
		//create reverse change
		
		//apply change
		return null;
		
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
