package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public abstract class HAPProcessorEntityExecutable {
	
	public abstract void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext);
	
	//return true: continue process, false: not
	public abstract boolean process(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext);
	
}
