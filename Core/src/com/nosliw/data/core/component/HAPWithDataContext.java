package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.script.context.HAPContextStructure;

public interface HAPWithDataContext {

	@HAPAttribute
	public static String CONTEXT = "context";
	
	HAPContextStructure getContextStructure();
	void setContextStructure(HAPContextStructure context);

	void cloneToDataContext(HAPWithDataContext dataContext);
}
