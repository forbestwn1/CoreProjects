package com.nosliw.data.core.imp.expression;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
	
	//mutiple messages
	private List<String> m_errorMsgs;
	
	// store all variable information in expression (variable name -- variable data type infor)
	// for variable that we don't know data type, its value in this map is null
	private Map<String, HAPDataTypeCriteria> m_varsInfo;
	
	//normalized variable information -- variable criteria with root from data type
	private Map<String, HAPDataTypeCriteria> m_normalizedVarsInfo;
	
	public HAPExpressionImp(HAPExpressionInfo expressionInfo, HAPOperand operand){
		this.m_errorMsgs = new ArrayList<String>();
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
	public String[] getErrorMessages() {
		if(this.m_errorMsgs==null || this.m_errorMsgs.size()==0)  return null;
		return this.m_errorMsgs.toArray(new String[0]);	
	}
	public void addErrorMessage(String msg){  this.m_errorMsgs.add(msg);  } 
	public void addErrorMessages(List<String> msgs){  this.m_errorMsgs.addAll(msgs);  } 
	
	public void buildNormalizedVariablesInfo(){
		//???????
		
	}
	
	public void mergeVariable(String variable, HAPDataTypeCriteria varInfo){
		
	}
}
