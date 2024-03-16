package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueContext;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String VALUECONTEXT = "valueContext";

	HAPDefinitionEntityValueContext getValueContextEntity();

	void setValueContextEntity(HAPDefinitionEntityValueContext valueContext);
	
}
