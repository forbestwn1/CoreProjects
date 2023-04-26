package com.nosliw.data.core.complex;

import java.util.Set;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;
import com.nosliw.data.core.domain.entity.HAPInfoAdapter;

public abstract class HAPProcessorEntityExecutable {
	
	public abstract void process(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapters, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext);

}
