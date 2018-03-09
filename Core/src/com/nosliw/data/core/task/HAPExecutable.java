package com.nosliw.data.core.task;

import com.nosliw.data.core.expression.HAPVariableInfo;

public interface HAPExecutable {

	//get output criteria
	HAPVariableInfo getOutput();
	
	//update variable in executable
	void updateVariable(HAPUpdateVariable updateVar);
	
}
