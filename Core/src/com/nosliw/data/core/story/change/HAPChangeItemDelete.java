package com.nosliw.data.core.story.change;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPChangeItemDelete extends HAPChangeItem{

	public static final String MYCHANGETYPE = HAPConstant.STORYDESIGN_CHANGETYPE_DELETE;

	public HAPChangeItemDelete() {}
	
	public HAPChangeItemDelete(String targetCategary, String targetId) {
		super(MYCHANGETYPE, targetCategary, targetId);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
