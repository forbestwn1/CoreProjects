package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPOperandAttribute;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandAttribute;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

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

	@Override
	public List<HAPManualWrapperOperand> getChildren(){   
		List<HAPManualWrapperOperand> out = new ArrayList<HAPManualWrapperOperand>();
		out.add(this.m_base);
		return out;
	}
	
	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		return null;
	}
	
}
