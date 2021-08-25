package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;

public interface HAPWithTask {

	@HAPAttribute
	public static String TASK = "task";

	HAPDefinitionTaskSuite getTaskSuite();
	
	void setTaskSuite(HAPDefinitionTaskSuite suite);
	
}
