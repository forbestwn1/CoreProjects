package com.nosliw.data.core.task;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPExecutable {

	HAPDataTypeCriteria getOutput();
	
	void updateVariable(HAPUpdateVariable updateVar);
	
}
