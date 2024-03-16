package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.core.application.division.manual.HAPManualEntityAdapter;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;

public class HAPDefinitionEntityDataAssciation extends HAPManualEntityAdapter{

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionDataAssociation dataAssciation) {    this.setAttributeValueObject(ATTR_DATAASSOCIATION, dataAssciation);    }
	public HAPDefinitionDataAssociation getDataAssociation() {   return (HAPDefinitionDataAssociation)this.getAttributeValue(ATTR_DATAASSOCIATION);     }

	
	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssciation out = new HAPDefinitionEntityDataAssciation();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
