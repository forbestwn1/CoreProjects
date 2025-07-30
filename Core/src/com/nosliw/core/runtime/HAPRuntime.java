package com.nosliw.core.runtime;

import com.nosliw.common.exception.HAPServiceData;

public interface HAPRuntime {

	public static final boolean isDemo = false;

	HAPRuntimeInfo getRuntimeInfo();
	
	//async request
	void executeTask(HAPRuntimeTask task);

	//sync request
	HAPServiceData executeTaskSync(HAPRuntimeTask task);

	//sync request
	//individual expression execute
//	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData);

	//sync request
	//individual data operation execute
//	public HAPServiceData executeDataOperationSync(HAPDataTypeId dataTypeId, String operation, List<HAPOperationParm> parmsData);
	
	void close();
	
	void start();

}
