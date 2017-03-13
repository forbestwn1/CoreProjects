package com.nosliw.data.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.expression.HAPExpressionInfo;
import com.nosliw.data.core.expression.HAPOperand;

/**
 * Parsed expression 
 */
public class HAPExpressionImp implements HAPExpression{

	// original expressiong
	private HAPExpressionInfo m_expressionInfo;

	// parsed expression
	private HAPOperand m_operand;
	
	private String m_errorMsg;
	
	// store all variable information in expression (variable name -- variable data type infor)
	// for variable that we don't know data type, its value in this map is null
	private Map<String, HAPDataTypeCriteria> m_varsInfo;
	
	public HAPExpressionImp(HAPExpressionInfo expressionInfo, HAPOperand operand){
		this.m_expressionInfo = expressionInfo;
		this.m_operand = operand;
		this.m_varsInfo = new LinkedHashMap<String, HAPDataTypeCriteria>();
		this.m_varsInfo.putAll(this.m_expressionInfo.getVariables());
	}
	
	@Override
	public HAPExpressionInfo getExpressionInfo() {		return this.m_expressionInfo;	}

	@Override
	public HAPOperand getOperand() {  return this.m_operand;  }

	@Override
	public Map<String, HAPDataTypeCriteria> getVariables() {		return this.m_varsInfo;	}
	
	public void setVariables(Map<String, HAPDataTypeCriteria> vars){
		this.m_varsInfo.clear();
		this.m_varsInfo.putAll(vars);
	}
	
	@Override
	public String getErrorMessage() {		return this.m_errorMsg;	}
	public void setErrorMessage(String msg){  this.m_errorMsg = msg;  } 
	
	public void mergeVariable(String variable, HAPDataTypeCriteria varInfo){
		
	}

}
