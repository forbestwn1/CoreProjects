package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinition;

public class HAPDefinitionEntityInDomainServiceProvider extends HAPDefinitionEntityInDomain{

	public static final String ATTR_SERVICEKEY = "serviceKey";

	public void setServiceKey(HAPKeyService serviceKey) {	this.setNormalAttributeObject(ATTR_SERVICEKEY, new HAPEmbededDefinition(serviceKey));	}

	public HAPKeyService getServiceKey() {	return (HAPKeyService)this.getNormalAttributeValue(ATTR_SERVICEKEY);	}
	
	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityInDomainServiceProvider out = new HAPDefinitionEntityInDomainServiceProvider();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
