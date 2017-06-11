package com.nosliw.data.core.runtime;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

public abstract class HAPExpressionTask {

	private HAPExpression m_expression;
	
	private String m_taskId;
	
	private HAPData m_result;
	
	public HAPExpressionTask(HAPExpression expression){
		this.m_expression = expression;
	}
	
	public abstract void setResult(HAPData data);
	
	public HAPData getResult(){ return this.m_result; }
	
	public HAPExpression getExpression(){return this.m_expression;}
	
	public String getTaskId(){  return this.m_taskId; }
	
	public void setTaskId(String taskId){ this.m_taskId = taskId;  }
}
