package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPOperandReference extends HAPOperand{

	@HAPAttribute
	public static final String RESOURCEID = "resourceId";

	@HAPAttribute
	public static final String VARMAPPING = "varMapping";
	
	@HAPAttribute
	public static final String VARRESOLVE = "varResolve";
	
	@HAPAttribute
	public static final String VARMATCHERS = "varMatchers";
	
}
