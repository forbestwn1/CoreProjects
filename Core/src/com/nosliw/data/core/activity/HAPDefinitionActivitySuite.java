package com.nosliw.data.core.activity;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.common.HAPWithValueStructure;

public interface HAPDefinitionActivitySuite extends 
	HAPWithEntityElement<HAPDefinitionActivity>, 
	HAPEntityInfoWritable, 
	HAPWithValueStructure{

	@HAPAttribute
	public static String ELEMENT = "element";

	HAPDefinitionActivitySuite cloneActivitySuiteDefinition();
}
