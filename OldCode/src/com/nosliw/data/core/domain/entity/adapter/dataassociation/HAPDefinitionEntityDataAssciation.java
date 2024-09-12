package com.nosliw.data.core.domain.entity.adapter.dataassociation;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualBrickAdapter;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;

public class HAPDefinitionEntityDataAssciation extends HAPManualBrickAdapter{

	public static final String ATTR_DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDefinitionDataAssociation dataAssciation) {    this.setAttributeValueObject(ATTR_DATAASSOCIATION, dataAssciation);    }
	public HAPDefinitionDataAssociation getDataAssociation() {   return (HAPDefinitionDataAssociation)this.getAttributeValueOfValue(ATTR_DATAASSOCIATION);     }

	
	@Override
	public HAPManualBrick cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityDataAssciation out = new HAPDefinitionEntityDataAssciation();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}

}
