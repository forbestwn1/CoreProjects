package com.nosliw.data.core.resource;

import com.nosliw.common.serialization.HAPSerializable;

public interface HAPEntityResourceDefinition extends HAPSerializable{

	HAPEntityResourceDefinition getChildResourceEntity(String child);
	
}
