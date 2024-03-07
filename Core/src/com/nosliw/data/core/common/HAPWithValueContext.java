package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueContext;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String VALUECONTEXT = "valueContext";

	HAPDefinitionEntityValueContext getValueContextEntity();

	void setValueContextEntity(HAPDefinitionEntityValueContext valueContext);
	
}
