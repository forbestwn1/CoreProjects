package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;

public class HAPManualDefinitionBlockScriptExpressionGroup extends HAPManualDefinitionBrickBlockSimple{

	public static final String VALUE = "value";
	
	public HAPManualDefinitionBlockScriptExpressionGroup() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100);
		this.setAttributeWithValueValue(VALUE, new HAPManualDefinitionContainerScriptExpression());
	}
	
	public HAPManualDefinitionContainerScriptExpression getValue() {
		return (HAPManualDefinitionContainerScriptExpression)this.getAttributeValueWithValue(HAPBlockDataExpressionGroup.VALUE);
	}
}
