package com.nosliw.data.core.expression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPResourceDefinitionContainerElementEntityImpComponent;
import com.nosliw.data.core.operand.HAPOperand;

public class HAPDefinitionExpressionSuiteElementEntity extends HAPResourceDefinitionContainerElementEntityImpComponent{

	@HAPAttribute
	public static String EXPRESSION = "expression";

	private HAPOperand m_operand;
	
	public HAPOperand getOperand() {    return this.m_operand;   }
	
	public void setOperand(HAPOperand operand) {   this.m_operand = operand;    }
}
