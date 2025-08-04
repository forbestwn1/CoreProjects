package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import com.nosliw.core.application.common.dataassociation.HAPDataAssociation;
import com.nosliw.core.application.division.manual.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.adapter.dataassociation.HAPAdapterDataAssociation;

public class HAPManualAdapterDataAssociation extends HAPManualBrickImp implements HAPAdapterDataAssociation{

	@Override
	public HAPDataAssociation getDataAssociation() {   return (HAPDataAssociation)this.getAttributeValueOfValue(DATAASSOCIATION);     }

	public void setDataAssciation(HAPDataAssociation dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }

}
