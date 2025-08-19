package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.expression.HAPBlockInteractiveInterfaceExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockInteractiveInterfaceExpression extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockInteractiveInterfaceExpression() {
		super(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100);
	}

	public HAPInteractiveExpression getValue() {   return (HAPInteractiveExpression)this.getAttributeValueOfValue(HAPBlockInteractiveInterfaceExpression.VALUE);  }
	public void setValue(HAPInteractiveExpression expressionInteractive) {      this.setAttributeValueWithValue(HAPManualBlockInteractiveInterfaceExpression.VALUE, expressionInteractive);       }
	
}
