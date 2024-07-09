package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.core.application.brick.adapter.dataassociationforexpression.HAPAdapterDataAssociationForExpression;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForExpression;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;

public class HAPManualAdapterDataAssociationForExpression extends HAPManualBrickAdapter implements HAPAdapterDataAssociationForExpression{

	public void setDataAssciation(HAPDataAssociationForExpression dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }
	public HAPDataAssociationForExpression getDataAssociation() {   return (HAPDataAssociationForExpression)this.getAttributeValueOfValue(DATAASSOCIATION);     }
}
