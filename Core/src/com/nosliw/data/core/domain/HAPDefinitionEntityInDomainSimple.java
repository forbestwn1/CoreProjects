package com.nosliw.data.core.domain;

public class HAPDefinitionEntityInDomainSimple extends HAPDefinitionEntityInDomain{

	public HAPDefinitionEntityInDomainSimple() {}
	
	public HAPDefinitionEntityInDomainSimple (String entityType) {
		super(entityType);
	}
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInDomainSimple out = new HAPDefinitionEntityInDomainSimple();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
