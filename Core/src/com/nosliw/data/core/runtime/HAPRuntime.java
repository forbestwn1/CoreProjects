package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;

public interface HAPRuntime {

	//async request
	void executeTask(HAPRuntimeTask task);

	//sync request
	HAPServiceData executeTaskSync(HAPRuntimeTask task);

	//sync request
	//individual expression execute
	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData);
	
	void close();
	
	void start();

}
