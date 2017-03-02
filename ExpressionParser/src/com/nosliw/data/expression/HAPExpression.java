package com.nosliw.data.expression;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeCriteria;

/**
 * Parsed expression 
 */
public class HAPExpression {

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
	
}
