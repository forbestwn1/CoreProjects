package com.nosliw.core.application.division.manual.common.dataexpression1;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.HAPOperandVariable;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandVariable;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPInfoCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;

public class HAPManualOperandVariable extends HAPManualOperand implements HAPOperandVariable{

	private String m_variableKey;
	
	private String m_variableName;
	
	public HAPManualOperandVariable(HAPDefinitionOperandVariable operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE, operandDefinition);
		this.m_variableName = operandDefinition.getVariableName();
	}

	@Override
	public String getVariableKey() {    return this.m_variableKey;    }
	public void setVariableKey(String varKey) {    this.m_variableKey = varKey;      }

	@Override
	public String getVariableName() {   return this.m_variableName;   }

	@Override
	public HAPMatchers discover(
			HAPContainerVariableInfo variablesInfo,
			HAPDataTypeCriteria expectCriteria, 
			HAPProcessTracker processTracker,
			HAPDataTypeHelper dataTypeHelper) {
		
		HAPInfoCriteria variableInfo = variablesInfo.getVaraibleCriteriaInfo(this.getVariableKey());
		
		HAPMatchers matchers = HAPUtilityCriteria.mergeVariableInfo(variableInfo, expectCriteria, dataTypeHelper);
		
		//set output criteria
		this.setOutputCriteria(variableInfo.getCriteria());

		//cal converter
		return matchers;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VARIABLENAME, m_variableName);
		if(m_variableKey!=null) {
			jsonMap.put(VARIABLEKEY, this.m_variableKey);
		}
	}
}
