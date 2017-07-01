package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

public abstract class HAPExecuteExpressionTask extends HAPRuntimeTask{

	private HAPExpression m_expression;
	
	Map<String, HAPData> m_variablesValue;
	
	public HAPExecuteExpressionTask(HAPExpression expression, Map<String, HAPData> variablesValue){
		this.m_expression = expression;
		this.m_variablesValue = variablesValue; 
	}
	
	public Map<String, HAPData> getVariablesValue(){  return this.m_variablesValue;  }

	public HAPExpression getExpression(){return this.m_expression;}
	
	public HAPData getExpressionDataResult(){ return (HAPData)this.getResult(); }

	@Override
	public String getTaskType(){  return "ExecuteExpression"; }
}
