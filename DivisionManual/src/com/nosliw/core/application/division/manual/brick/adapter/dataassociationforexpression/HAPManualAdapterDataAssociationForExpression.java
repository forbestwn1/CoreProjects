package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;

public class HAPManualAdapterDataAssociationForExpression extends HAPManualDefinitionBrickAdapter{

	public static final String DEFINITION = "definition";

	public HAPManualAdapterDataAssociationForExpression() {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100);
	}
	
	public void setDataAssciation(HAPManualDataAssociationForExpression dataAssciation) {    this.setAttributeWithValueValue(DEFINITION, dataAssciation);    }
	public HAPManualDataAssociationForExpression getDataAssociation() {   return (HAPManualDataAssociationForExpression)this.getAttributeValueWithValue(DEFINITION);     }
	
}
