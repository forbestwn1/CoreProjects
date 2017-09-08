package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;

public interface HAPRuntime {

	//async request
	public void executeTask(HAPRuntimeTask task);

	//sync request
	public HAPData executeExpression(HAPExpression expression, Map<String, HAPData> varData);
	
	void close();
	
	void start();

}
