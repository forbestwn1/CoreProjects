package com.nosliw.data.core.domain.entity;

import java.util.Set;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainExecutable;

public abstract class HAPProcessorEntityExecutable {
	
	public abstract void process(HAPInfoEntityInDomainExecutable entityInfo, Set<HAPInfoAdapter> adapters, HAPInfoEntityInDomainExecutable parentEntityInfo, HAPContextProcessor processContext);

}
