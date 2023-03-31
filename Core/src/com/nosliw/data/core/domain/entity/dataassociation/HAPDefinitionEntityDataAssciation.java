package com.nosliw.data.core.domain.entity.dataassociation;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainSimple;
import com.nosliw.data.core.domain.entity.HAPEmbededDefinitionWithValue;

public class HAPDefinitionEntityDataAssciation extends HAPDefinitionEntityInDomainSimple{

	public static final String ENTITY_TYPE = HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAASSOCIATION;

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionDataAssociation dataAssciation) {    this.setNormalAttribute(ATTR_DATAASSOCIATION, new HAPEmbededDefinitionWithValue(dataAssciation));    }
	public HAPDefinitionDataAssociation getDataAssociation() {   return (HAPDefinitionDataAssociation)this.getNormalAttributeWithValue(ATTR_DATAASSOCIATION).getValue().getValue();     }

	
	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssciation out = new HAPDefinitionEntityDataAssciation();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
