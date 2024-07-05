package com.nosliw.core.application.division.manual.common.dataexpression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandAttribute;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandAttribute;

public class HAPManualOperandAttribute extends HAPManualOperand implements HAPOperandAttribute{

	private String m_attribute;
	
	private HAPManualWrapperOperand m_base;
	
	public HAPManualOperandAttribute(HAPDefinitionOperandAttribute operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION, operandDefinition);
		this.m_attribute = operandDefinition.getAttribute();
	}

	@Override
	public String getAttribute() {    return this.m_attribute;    }

	@Override
	public HAPOperand getBase() {   return this.m_base.getOperand();    }   
	public void setBase(HAPManualOperand base) {   this.m_base = new HAPManualWrapperOperand(base);    }
	
}
