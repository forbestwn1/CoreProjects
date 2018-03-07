package com.nosliw.data.core.task;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPExecutable {

	//get output criteria
	HAPDataTypeCriteria getOutput();
	
	//update variable in executable
	void updateVariable(HAPUpdateVariable updateVar);
	
}
