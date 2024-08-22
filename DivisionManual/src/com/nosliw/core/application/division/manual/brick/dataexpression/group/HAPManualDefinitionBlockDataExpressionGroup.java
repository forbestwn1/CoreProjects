package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockDataExpressionGroup extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockDataExpressionGroup() {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100);
		this.setAttributeValueWithValue(HAPBlockDataExpressionGroup.VALUE, new HAPManualDefinitionContainerDataExpression());
	}

	public HAPManualDefinitionContainerDataExpression getValue() {
		return (HAPManualDefinitionContainerDataExpression)this.getAttributeValueOfValue(HAPBlockDataExpressionGroup.VALUE);
	}
	
}
