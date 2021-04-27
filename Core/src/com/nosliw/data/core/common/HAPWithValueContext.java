package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.structure.value.HAPContextStructureValue;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String CONTEXT = "context";
	
	HAPContextStructureValue getValueContext();
	void setValueContext(HAPContextStructureValue context);

	void cloneToValueContext(HAPWithValueContext dataContext);
}
