package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPData;

@HAPEntityWithAttribute
public abstract class HAPRuntimeTaskExecuteExpression extends HAPRuntimeTask{

	final public static String TASK = "ExecuteExpression"; 
	
	@HAPAttribute
	public static String EXPRESSION = "expression";

	@HAPAttribute
	public static String VARIABLESVALUE = "variablesValue";

	
	private HAPExpressionExecute m_expression;
	
	private Map<String, HAPData> m_variablesValue;

	private Map<String, HAPData> m_referencesValue;
	
	public HAPRuntimeTaskExecuteExpression(HAPExpressionExecute expression, Map<String, HAPData> variablesValue, Map<String, HAPData> referencesValue){
		this.m_expression = expression;
		this.m_variablesValue = variablesValue; 
		this.m_referencesValue = referencesValue;
	}
	
	public Map<String, HAPData> getVariablesValue(){  return this.m_variablesValue;  }

	public Map<String, HAPData> getReferencesValue(){  return this.m_referencesValue;  }
	
	public HAPExpressionExecute getExpression(){return this.m_expression;}
	
	public HAPData getExpressionDataResult(){ return (HAPData)this.getResult(); }

	@Override
	public String getTaskType(){  return TASK; }

}
