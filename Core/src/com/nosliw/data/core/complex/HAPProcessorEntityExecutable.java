package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;

public abstract class HAPProcessorEntityExecutable {
	
	public abstract void process(HAPInfoEntityInDomainExecutable entityInfo, Object adapter, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext);

}
