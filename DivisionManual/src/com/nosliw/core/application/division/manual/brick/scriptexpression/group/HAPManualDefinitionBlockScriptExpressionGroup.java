package com.nosliw.core.application.division.manual.brick.scriptexpression.group;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.group.HAPBlockDataExpressionGroup;
import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockScriptExpressionGroup extends HAPManualDefinitionBrick{

	public static final String VALUE = "value";
	
	public HAPManualDefinitionBlockScriptExpressionGroup() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONGROUP_100);
		this.setAttributeValueWithValue(VALUE, new HAPDefinitionContainerScriptExpression());
	}
	
	public HAPDefinitionContainerScriptExpression getValue() {
		return (HAPDefinitionContainerScriptExpression)this.getAttributeValueOfValue(HAPBlockDataExpressionGroup.VALUE);
	}
}
