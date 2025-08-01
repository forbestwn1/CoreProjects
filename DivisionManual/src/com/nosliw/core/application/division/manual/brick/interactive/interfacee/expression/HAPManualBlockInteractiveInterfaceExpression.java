package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.interactive.interfacee.expression.HAPBlockInteractiveInterfaceExpression;

public class HAPManualBlockInteractiveInterfaceExpression extends HAPManualBrickImp implements HAPBlockInteractiveInterfaceExpression{

	@Override
	public HAPInteractiveExpression getValue() {   return (HAPInteractiveExpression)this.getAttributeValueOfValue(HAPBlockInteractiveInterfaceExpression.VALUE);  }

	public void setValue(HAPInteractiveExpression expressionInteractive) {      this.setAttributeValueWithValue(HAPManualBlockInteractiveInterfaceExpression.VALUE, expressionInteractive);       }

}
