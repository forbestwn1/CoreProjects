package com.nosliw.core.application.division.manual.common.dataexpression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.HAPOperandVariable;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandVariable;
import com.nosliw.core.application.common.valueport.HAPContainerVariableInfo;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPManualOperandVariable extends HAPManualOperand implements HAPOperandVariable{

	private HAPIdElement m_variableId;
	
	private String m_variableName;
	
	public HAPManualOperandVariable(HAPDefinitionOperandVariable operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE, operandDefinition);
		this.m_variableName = operandDefinition.getVariableName();
	}

	@Override
	public HAPIdElement getVariableId() {    return this.m_variableId;    }
	public void setVariableId(HAPIdElement varId) {    this.m_variableId = varId;      }

	@Override
	public String getVariableName() {   return this.m_variableName;   }

	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		
		HAPInfoCriteria variableInfo = variablesInfo.getVaraibleCriteriaInfo(this.getVariableId());
		
		HAPMatchers matchers = HAPUtilityCriteria.mergeVariableInfo(variableInfo, expectCriteria, dataTypeHelper);
		
		//set output criteria
		this.setOutputCriteria(variableInfo.getCriteria());

		//cal converter
		return matchers;
	}
}
