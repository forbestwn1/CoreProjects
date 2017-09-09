package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

public interface HAPRuntime {

	//async request
	void executeTask(HAPRuntimeTask task);

	//sync request
	HAPServiceData executeTaskSync(HAPRuntimeTask task);

	//sync request
	public HAPServiceData executeExpression(HAPExpression expression, Map<String, HAPData> varData);
	
	void close();
	
	void start();

}
