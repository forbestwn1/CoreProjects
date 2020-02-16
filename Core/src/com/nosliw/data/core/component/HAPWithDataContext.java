package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.script.context.HAPContextGroup;

public interface HAPWithDataContext {

	@HAPAttribute
	public static String CONTEXT = "context";
	
	HAPContextGroup getContext();
	void setContext(HAPContextGroup context);

	void cloneToDataContext(HAPWithDataContext dataContext);
}
