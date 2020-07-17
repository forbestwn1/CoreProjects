package com.nosliw.data.core.story.design;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPChangeItemNew extends HAPChangeItem{

	public static final String MYCHANGETYPE = HAPConstant.STORYDESIGN_CHANGETYPE_NEW;

	@HAPAttribute
	public static final String ELEMENT = "element";

	public HAPChangeItemNew() {}
	
	public HAPChangeItemNew(String targetCategary, String targetId) {
		super(MYCHANGETYPE, targetCategary, targetId);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ELEMENT, this.getStory().getElement(this.getTargetCategary(), this.getTargetId()).toStringValue(HAPSerializationFormat.JSON));
	}
}
