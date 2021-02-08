package com.nosliw.data.core.story.change;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPUtilityChange {

	public static HAPChangeItem buildChangePatch(HAPStoryElement element, String path, Object value) {	return new HAPChangeItemPatch(element.getElementId(), path, value);	}
	public static HAPChangeItem buildChangePatch(HAPReferenceElement elementRef, String path, Object value) {	return new HAPChangeItemPatch(elementRef, path, value);	}
	
	public static boolean isElementChange(HAPChangeItem change) {
		String type = change.getChangeType();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_NEW))  return true;
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_PATCH))  return true;
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE))  return true;
		else return false;
	}
	
	public static HAPIdElement getChangeTargetElementId(HAPChangeItem change) {
		String type = change.getChangeType();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_NEW))  return ((HAPChangeItemNew)change).getElement().getElementId();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_PATCH))  return ((HAPChangeItemPatch)change).getTargetElementId();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE))  return ((HAPChangeItemDelete)change).getTargetElementId();
		return null;
	}
	
	public static HAPChangeItemStoryInfo newStoryIndexChange(HAPStory story) {
		HAPChangeItemStoryInfo out = new HAPChangeItemStoryInfo(HAPConstantShared.STORY_INFO_IDINDEX, story.getInfo().getValue(HAPConstantShared.STORY_INFO_IDINDEX));
		out.setRevertable(false);
		return out;
	}
	
}
