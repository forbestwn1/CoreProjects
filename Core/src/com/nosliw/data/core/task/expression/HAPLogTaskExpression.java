package com.nosliw.data.core.task.expression;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.HAPLog;
import com.nosliw.data.core.task.expression.HAPExecutableTaskExpression;

public class HAPLogTaskExpression extends HAPLog{

	private String m_name;
	
	private String m_id;
	
	private HAPData m_result;
	
	public HAPLogTaskExpression(HAPExecutableTaskExpression expTaskExe) {
		this.m_name = expTaskExe.getName();
	}

	public void setResult(HAPData result) {  this.m_result = result;  }
	
}
