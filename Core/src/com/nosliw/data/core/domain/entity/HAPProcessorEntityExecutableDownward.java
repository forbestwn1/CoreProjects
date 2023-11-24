package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;

public abstract class HAPProcessorEntityExecutableDownward {
	
	public abstract void processComplexRoot(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext);
	
	//process attribute under entity
	//return true: continue process, false: not
	public abstract boolean processAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext);
	
	//after process attribute
	public void postProcessAttribute(HAPExecutableEntity parentEntity, String attribute, HAPContextProcessor processContext) {}
	
	public void postProcessComplexRoot(HAPExecutableEntityComplex complexEntity, HAPContextProcessor processContext) {}
}
