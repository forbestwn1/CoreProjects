package com.nosliw.data.core.task;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.expression.HAPExecutableTaskExpression;

public class HAPLogTask {

	private String m_name;
	
	private String m_id;
	
	private HAPData m_result;
	
	public HAPLogTask(HAPExecutableTask expTaskExe) {
		this.m_name = expTaskExe.getName();
	}

	public void setResult(HAPData result) {  this.m_result = result;  }
}
