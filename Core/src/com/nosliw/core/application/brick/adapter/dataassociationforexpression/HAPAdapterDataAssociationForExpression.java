package com.nosliw.core.application.brick.adapter.dataassociationforexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationForExpression;
import com.nosliw.core.application.division.manual.executable.HAPBrickAdapter;

@HAPEntityWithAttribute
public class HAPAdapterDataAssociationForExpression extends HAPBrickAdapter{

	@HAPAttribute
	public static final String DATAASSOCIATION = "dataAssociation";
	
	public void setDataAssciation(HAPDataAssociationForExpression dataAssciation) {    this.setAttributeValueWithValue(DATAASSOCIATION, dataAssciation);    }
	public HAPDataAssociationForExpression getDataAssociation() {   return (HAPDataAssociationForExpression)this.getAttributeValueOfValue(DATAASSOCIATION);     }
}
