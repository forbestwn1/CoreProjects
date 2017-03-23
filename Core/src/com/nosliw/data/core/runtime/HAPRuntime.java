package com.nosliw.data.core.runtime;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

/**
 * Runtime executor used to execute expression in particular runtime environment  
 */
public interface HAPRuntime {

	HAPRuntimeInfo getRuntimeInfo();
	
	HAPData executeExpression(HAPExpression expression);
	
	HAPResourceManager getResourceManager();
	
}
