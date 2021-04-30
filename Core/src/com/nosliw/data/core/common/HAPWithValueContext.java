package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.structure.value.HAPStructureValue;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String CONTEXT = "context";
	
	HAPStructureValue getValueContext();
	void setValueContext(HAPStructureValue context);

	void cloneToValueContext(HAPWithValueContext dataContext);
}
