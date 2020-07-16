package com.nosliw.data.core.story.design;

import com.nosliw.common.utils.HAPConstant;

public class HAPChangeItemNew extends HAPChangeItem{

	public static final String MYCHANGETYPE = HAPConstant.STORYDESIGN_CHANGETYPE_NEW;

	public HAPChangeItemNew() {}
	
	public HAPChangeItemNew(String targetCategary, String targetId) {
		super(MYCHANGETYPE, targetCategary, targetId);
	}

	

}
