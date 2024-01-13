package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainAdapter;

public class HAPDefinitionEntityDataAssciation extends HAPDefinitionEntityInDomainAdapter{

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionDataAssociation dataAssciation) {    this.setAttributeValueObject(ATTR_DATAASSOCIATION, dataAssciation);    }
	public HAPDefinitionDataAssociation getDataAssociation() {   return (HAPDefinitionDataAssociation)this.getAttributeValue(ATTR_DATAASSOCIATION);     }

	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssciation out = new HAPDefinitionEntityDataAssciation();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
