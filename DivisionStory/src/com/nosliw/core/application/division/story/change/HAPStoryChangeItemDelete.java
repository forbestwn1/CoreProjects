package com.nosliw.core.application.division.story.change;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;

public class HAPStoryChangeItemDelete extends HAPStoryChangeItemModifyElement{

	public static final String MYCHANGETYPE = HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE;

	public HAPStoryChangeItemDelete() {
		super(MYCHANGETYPE);
	}
	
	public HAPStoryChangeItemDelete(HAPStoryReferenceElement targetReference) {
		super(MYCHANGETYPE, targetReference);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
