package com.nosliw.data.core.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.common.HAPWithValueContext;

public interface HAPDefinitionTaskSuite extends 
	HAPWithEntityElement<HAPDefinitionTask>, 
	HAPEntityInfoWritable, 
	HAPWithValueContext{

	@HAPAttribute
	public static String ELEMENT = "element";

	HAPDefinitionTaskSuite cloneTaskSuiteDefinition();
}
