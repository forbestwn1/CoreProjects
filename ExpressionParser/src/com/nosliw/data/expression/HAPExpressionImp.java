package com.nosliw.data.expression;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPOperand;

/**
 * Parsed expression 
 */
public class HAPExpressionImp implements HAPExpression{

	private String m_name;
	
	// original expressiong
	private String m_expression;

	// parsed expression
	private HAPOperand m_operand;
	
	// store all variable information in expression (variable name -- variable data type infor)
	// for variable that we don't know data type, its value in this map is null
	private Map<String, HAPDataTypeCriteria> m_varsInfo;
	
	//store constant data to used in expression
	private Map<String, HAPData> m_constantDatas;

	@Override
	public HAPExpressionInfo getExpressionInfo() {
		return null;
	}

	@Override
	public String getOperand() {
		return null;
	}

	@Override
	public Map<String, HAPDataTypeCriteria> getVariables() {
		return null;
	} 
	
}
