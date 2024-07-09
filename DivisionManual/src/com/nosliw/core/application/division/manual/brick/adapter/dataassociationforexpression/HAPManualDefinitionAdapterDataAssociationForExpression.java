package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;

public class HAPManualDefinitionAdapterDataAssociationForExpression extends HAPManualDefinitionBrickAdapter{

	public static final String DEFINITION = "definition";

	public HAPManualDefinitionAdapterDataAssociationForExpression() {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100);
	}
	
	public void setDataAssciation(HAPManualDataAssociationForExpression dataAssciation) {    this.setAttributeWithValueValue(DEFINITION, dataAssciation);    }
	public HAPManualDataAssociationForExpression getDataAssociation() {   return (HAPManualDataAssociationForExpression)this.getAttributeValueWithValue(DEFINITION);     }
	
}
