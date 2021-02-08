package com.nosliw.data.core.story.change;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.story.HAPReferenceElement;

public class HAPChangeItemDelete extends HAPChangeItemModifyElement{

	public static final String MYCHANGETYPE = HAPConstantShared.STORYDESIGN_CHANGETYPE_DELETE;

	public HAPChangeItemDelete() {
		super(MYCHANGETYPE);
	}
	
	public HAPChangeItemDelete(HAPReferenceElement targetReference) {
		super(MYCHANGETYPE, targetReference);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
