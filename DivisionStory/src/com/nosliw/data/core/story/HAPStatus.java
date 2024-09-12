package com.nosliw.data.core.story;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPStatus extends HAPSerializableImp{

	public HAPStatus cloneStatus() {
		return new HAPStatus();
	}
	
}
