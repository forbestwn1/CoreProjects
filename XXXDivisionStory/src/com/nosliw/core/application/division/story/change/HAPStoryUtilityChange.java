package com.nosliw.core.application.division.story.change;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;

public class HAPStoryUtilityChange {

	public static HAPStoryChangeItem buildChangePatch(HAPStoryElement element, String path, Object value) {	return new HAPStoryChangeItemPatch(element.getElementId(), path, value);	}
	public static HAPStoryChangeItem buildChangePatch(HAPStoryReferenceElement elementRef, String path, Object value) {	return new HAPStoryChangeItemPatch(elementRef, path, value);	}
	
	public static boolean isElementChange(HAPStoryChangeItem change) {
		String type = change.getChangeType();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_NEW))  return true;
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_PATCH))  return true;
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE))  return true;
		else return false;
	}
	
	public static HAPStoryIdElement getChangeTargetElementId(HAPStoryChangeItem change) {
		String type = change.getChangeType();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_NEW))  return ((HAPStoryChangeItemNew)change).getElement().getElementId();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_PATCH))  return ((HAPStoryChangeItemPatch)change).getTargetElementId();
		if(type.equals(HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE))  return ((HAPStoryChangeItemDelete)change).getTargetElementId();
		return null;
	}
	
	public static HAPStoryChangeItemStoryInfo newStoryIndexChange(HAPStoryStory story) {
		HAPStoryChangeItemStoryInfo out = new HAPStoryChangeItemStoryInfo(HAPConstantShared.STORY_INFO_IDINDEX, story.getInfo().getValue(HAPConstantShared.STORY_INFO_IDINDEX));
		out.setRevertable(false);
		return out;
	}
	
}
