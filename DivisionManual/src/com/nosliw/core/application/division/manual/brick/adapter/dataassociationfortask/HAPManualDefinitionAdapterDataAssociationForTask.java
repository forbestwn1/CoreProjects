package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionAdapterDataAssociationForTask extends HAPManualDefinitionBrick{

	public static final String DEFINITION = "definition";

	public HAPManualDefinitionAdapterDataAssociationForTask() {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100);
	}
	
	public void setDataAssciation(HAPManualDataAssociationForTask dataAssciation) {    this.setAttributeWithValueValue(DEFINITION, dataAssciation);    }
	public HAPManualDataAssociationForTask getDataAssociation() {   return (HAPManualDataAssociationForTask)this.getAttributeValueWithValue(DEFINITION);     }
	
}
