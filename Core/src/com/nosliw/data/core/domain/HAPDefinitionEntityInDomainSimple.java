package com.nosliw.data.core.domain;

import com.nosliw.common.interfac.HAPEntityOrReference;

public class HAPDefinitionEntityInDomainSimple extends HAPDefinitionEntityInDomain{

	@Override
	public HAPEntityOrReference getChild(String path) {  return null;  }

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInDomainSimple out = new HAPDefinitionEntityInDomainSimple();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
