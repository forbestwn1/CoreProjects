package com.nosliw.core.application.division.manual.common.dataexpression;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperand;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;

public class HAPManualOperand extends HAPSerializableImp implements HAPOperand{

	private String m_type;
	
	private HAPDataTypeCriteria m_outputCriteria;
	
	private HAPDefinitionOperand m_operandDefinition;
	
	public HAPManualOperand(String type, HAPDefinitionOperand operandDefinition) {
		this.m_type = type;
		this.m_operandDefinition = operandDefinition;
	}
	
	@Override
	public String getType() {   return this.m_type;  }

	@Override
	public HAPDataTypeCriteria getOutputCriteria() {   return this.m_outputCriteria;  }

}
