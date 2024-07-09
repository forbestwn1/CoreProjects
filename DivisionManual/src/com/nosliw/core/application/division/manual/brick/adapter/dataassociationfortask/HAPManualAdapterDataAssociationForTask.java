package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import com.nosliw.core.application.brick.adapter.dataassociationfortask.HAPAdapterDataAssociationForTask;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForTask;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;

public class HAPManualAdapterDataAssociationForTask extends HAPManualBrickAdapter implements HAPAdapterDataAssociationForTask{

	public void setDataAssciation(HAPDataAssociationForTask dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }
	public HAPDataAssociationForTask getDataAssociation() {   return (HAPDataAssociationForTask)this.getAttributeValueOfValue(DATAASSOCIATION);     }
}
