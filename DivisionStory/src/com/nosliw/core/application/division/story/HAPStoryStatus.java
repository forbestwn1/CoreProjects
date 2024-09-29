package com.nosliw.core.application.division.story;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPStoryStatus extends HAPSerializableImp{

	public HAPStoryStatus cloneStatus() {
		return new HAPStoryStatus();
	}
	
}
