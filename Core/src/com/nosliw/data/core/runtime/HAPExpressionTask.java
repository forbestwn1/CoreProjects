package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

public abstract class HAPExpressionTask {

	private HAPExpression m_expression;
	
	private String m_taskId;
	
	private HAPData m_result;
	
	Map<String, HAPData> m_variablesValue;
	
	public HAPExpressionTask(HAPExpression expression, Map<String, HAPData> variablesValue){
		this.m_expression = expression;
	}
	
	//expression result call back
	public abstract void setResult(HAPData data);
	
	//resource loaded call back
	public abstract void resourceLoaded();
	
	public Map<String, HAPData> getVariablesValue(){  return this.m_variablesValue;  }
	
	public HAPData getResult(){ return this.m_result; }
	
	public HAPExpression getExpression(){return this.m_expression;}
	
	public String getTaskId(){  return this.m_taskId; }
	
	public void setTaskId(String taskId){ this.m_taskId = taskId;  }
}
