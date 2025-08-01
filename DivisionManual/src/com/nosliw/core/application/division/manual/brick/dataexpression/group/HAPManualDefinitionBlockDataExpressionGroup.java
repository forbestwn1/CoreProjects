package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.dataexpression.group.HAPBlockDataExpressionGroup;

public class HAPManualDefinitionBlockDataExpressionGroup extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockDataExpressionGroup() {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100);
		this.setAttributeValueWithValue(HAPBlockDataExpressionGroup.VALUE, new HAPManualDefinitionContainerDataExpression());
	}

	public HAPManualDefinitionContainerDataExpression getValue() {
		return (HAPManualDefinitionContainerDataExpression)this.getAttributeValueOfValue(HAPBlockDataExpressionGroup.VALUE);
	}
	
}
