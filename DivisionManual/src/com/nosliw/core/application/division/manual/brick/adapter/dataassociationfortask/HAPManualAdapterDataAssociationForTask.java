package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import com.nosliw.core.application.brick.adapter.dataassociationfortask.HAPAdapterDataAssociationForTask;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForTask;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualAdapterDataAssociationForTask extends HAPManualBrickImp implements HAPAdapterDataAssociationForTask{

	public void setDataAssciation(HAPDataAssociationForTask dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }
	@Override
	public HAPDataAssociationForTask getDataAssociation() {   return (HAPDataAssociationForTask)this.getAttributeValueOfValue(DATAASSOCIATION);     }
}
