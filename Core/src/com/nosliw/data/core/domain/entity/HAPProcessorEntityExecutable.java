package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public abstract class HAPProcessorEntityExecutable {
	
	public abstract void processComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext);
	
	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext);
	
	//after process attribute
	public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {}
	
	public void postProcessComplexRoot(HAPIdEntityInDomain entityId, HAPContextProcessor processContext) {}
}
