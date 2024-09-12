package com.nosliw.data.core.domain.definition;

import com.nosliw.data.core.resource.HAPInfoResourceLocation;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public interface HAPPluginResourceDefinition {

	HAPInfoResourceLocation getSimpleResourceLocation(HAPResourceIdSimple resourceId);

	HAPInfoResourceLocation getLocalResourceLocation(HAPResourceIdSimple baseResourceId, HAPResourceIdLocal resourceId);
	
}
