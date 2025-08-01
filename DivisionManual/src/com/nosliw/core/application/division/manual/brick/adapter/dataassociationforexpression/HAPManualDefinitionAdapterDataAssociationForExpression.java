package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;

public class HAPManualDefinitionAdapterDataAssociationForExpression extends HAPManualDefinitionBrick{

	public static final String DEFINITION = "definition";

	public HAPManualDefinitionAdapterDataAssociationForExpression() {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100);
	}
	
	public void setDataAssciation(HAPManualDataAssociationForExpression dataAssciation) {    this.setAttributeValueWithValue(DEFINITION, dataAssciation);    }
	public HAPManualDataAssociationForExpression getDataAssociation() {   return (HAPManualDataAssociationForExpression)this.getAttributeValueOfValue(DEFINITION);     }
	
}
