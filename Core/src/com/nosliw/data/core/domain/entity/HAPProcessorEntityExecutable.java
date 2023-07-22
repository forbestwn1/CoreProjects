package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public abstract class HAPProcessorEntityExecutable {
	
	public abstract void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext);
	
	public abstract void process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext);
	
}
