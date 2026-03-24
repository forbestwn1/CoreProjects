package com.nosliw.core.application.brick.interactive.interfacee.expression;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;

public class HAPBlockInteractiveInterfaceExpressionImp extends HAPBrickImp implements HAPBlockInteractiveInterfaceExpression{

	public HAPBlockInteractiveInterfaceExpressionImp(){
		this.setBrickType(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100);
	}
	
	@Override
	public HAPInteractiveExpression getValue() {    return (HAPInteractiveExpression)this.getAttributeValueOfValue(VALUE);  }
	@Override
	public void setValue(HAPInteractiveExpression value) {   this.setAttributeValueWithValue(VALUE, value);      }

}