package com.nosliw.data.core.domain.entity;

import java.util.Set;

import com.nosliw.data.core.domain.valueport.HAPDefinitionValuePort;

public abstract class HAPDefinitionEntityInDomainBlock extends HAPDefinitionEntityInDomain{

	public HAPDefinitionEntityInDomainBlock () {}

	protected HAPDefinitionEntityInDomainBlock (String entityType) {
		super(entityType);
	}

	public abstract Set<HAPDefinitionValuePort> getValuePorts();
}
