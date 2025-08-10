package com.nosliw.core.application.division.manual.brick.adapter.dataassociationforexpression;

import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociationForExpression;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;

public class HAPManualDefinitionAdapterDataAssociationForExpression extends HAPManualDefinitionBrick{

	public static final String DEFINITION = "definition";

	public HAPManualDefinitionAdapterDataAssociationForExpression() {
		super(HAPEnumBrickType.DATAASSOCIATIONFOREXPRESSION_100);
	}
	
	public void setDataAssciation(HAPDefinitionDataAssociationForExpression dataAssciation) {    this.setAttributeValueWithValue(DEFINITION, dataAssciation);    }
	public HAPDefinitionDataAssociationForExpression getDataAssociation() {   return (HAPDefinitionDataAssociationForExpression)this.getAttributeValueOfValue(DEFINITION);     }
	
}
