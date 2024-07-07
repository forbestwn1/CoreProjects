package com.nosliw.core.application.division.manual.common.dataexpression;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.common.dataexpression.HAPDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.valueport.HAPIdElement;

public class HAPManualDataExpression extends HAPSerializableImp implements HAPDataExpression{

	private HAPManualWrapperOperand m_operand;
	
	private List<HAPIdElement> m_variablesInfo;

	public HAPManualDataExpression(HAPManualOperand operand) {
		this.m_variablesInfo = new ArrayList<HAPIdElement>();
		this.m_operand = new HAPManualWrapperOperand(operand);
	}
	
	@Override
	public HAPOperand getOperand() {   return this.m_operand.getOperand();  }
	public HAPManualWrapperOperand getOperandWrapper() {   return this.m_operand;     }

	@Override
	public List<HAPIdElement> getVariablesInfo() {   return this.m_variablesInfo;   }

}
