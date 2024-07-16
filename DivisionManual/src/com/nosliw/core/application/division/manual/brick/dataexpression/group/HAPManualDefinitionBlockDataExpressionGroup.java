package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockComplex;

public class HAPManualDefinitionBlockDataExpressionGroup extends HAPManualDefinitionBrickBlockComplex{

	public HAPManualDefinitionBlockDataExpressionGroup() {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100);
		this.setAttributeWithValueValue(HAPBlockDataExpressionGroup.VALUE, new HAPManualDefinitionContainerDataExpression());
	}

	public HAPManualDefinitionContainerDataExpression getValue() {
		return (HAPManualDefinitionContainerDataExpression)this.getAttributeValueWithValue(HAPBlockDataExpressionGroup.VALUE);
	}
	
}
