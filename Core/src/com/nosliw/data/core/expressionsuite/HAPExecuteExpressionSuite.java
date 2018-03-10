package com.nosliw.data.core.expressionsuite;

import java.util.Map;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.operand.HAPOperandWrapper;
import com.nosliw.data.core.runtime.HAPExecuteExpression;

public class HAPExecuteExpressionSuite implements HAPExecuteExpression{

	private HAPOperandWrapper m_operand;
	
	private String m_id;
	
	public HAPExecuteExpressionSuite(String id, HAPOperand operand) {
		this.m_operand = new HAPOperandWrapper(operand);
		this.m_id = id;
	}
	
	@Override
	public HAPOperandWrapper getOperand() {		return this.m_operand;	}

	@Override
	public Map<String, HAPMatchers> getVariableMatchers() {
		return null;
	}

	public void discover(
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			HAPDataTypeCriteria expectOutput,
			HAPDataTypeHelper dataTypeHelper) {

		Map<String, HAPVariableInfo> discoveredVarsInf = HAPOperandUtility.discover(this.getOperand().getOperand(), parentVariablesInfo, expectOutput, dataTypeHelper);
		parentVariablesInfo.clear();
		parentVariablesInfo.putAll(discoveredVarsInf);
		
	}

	@Override
	public String getId() {		return this.m_id;	}
	
}
