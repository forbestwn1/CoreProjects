package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrickAdapter;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForTask;

public class HAPManualAdapterDataAssociationForTask extends HAPManualBrickAdapter{

	public static final String DEFINITION = "definition";

	public HAPManualAdapterDataAssociationForTask() {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100);
	}
	
	public void setDataAssciation(HAPManualDataAssociationForTask dataAssciation) {    this.setAttributeWithValueValue(DEFINITION, dataAssciation);    }
	public HAPManualDataAssociationForTask getDataAssociation() {   return (HAPManualDataAssociationForTask)this.getAttributeValueWithValue(DEFINITION);     }
	
}
