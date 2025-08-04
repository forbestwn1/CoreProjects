package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForExpression;
import com.nosliw.core.application.division.manual.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.adapter.dataassociationforexpression.HAPAdapterDataAssociationForExpression;

public class HAPManualAdapterDataAssociationForExpression extends HAPManualBrickImp implements HAPAdapterDataAssociationForExpression{

	public void setDataAssciation(HAPDataAssociationForExpression dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }
	@Override
	public HAPDataAssociationForExpression getDataAssociation() {   return (HAPDataAssociationForExpression)this.getAttributeValueOfValue(DATAASSOCIATION);     }
}
