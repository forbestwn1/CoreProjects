package com.nosliw.core.application.division.manual.brick.dataexpression.group;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockComplex;

public class HAPManualBlockDataExpressionGroup extends HAPManualDefinitionBrickBlockComplex{

	public HAPManualBlockDataExpressionGroup() {
		super(HAPEnumBrickType.DATAEXPRESSIONGROUP_100);
		this.setAttributeWithValueValue(HAPBlockDataExpressionGroup.VALUE, new HAPManualDataExpressionGroup());
	}

	public HAPManualDataExpressionGroup getValue() {
		return (HAPManualDataExpressionGroup)this.getAttributeValueWithValue(HAPBlockDataExpressionGroup.VALUE);
	}
	
}
