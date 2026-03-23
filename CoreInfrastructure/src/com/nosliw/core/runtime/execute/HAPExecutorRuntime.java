package com.nosliw.core.runtime.execute;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public interface HAPExecutorRuntime {

	public static final boolean isDemo = false;

	HAPRuntimeInfo getRuntimeInfo();
	
	//async request
	void executeTask(HAPTaskRuntime task);

	//sync request
	HAPServiceData executeTaskSync(HAPTaskRuntime task);

	//sync request
	//individual expression execute
//	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData);

	//sync request
	//individual data operation execute
//	public HAPServiceData executeDataOperationSync(HAPDataTypeId dataTypeId, String operation, List<HAPOperationParm> parmsData);
	
	void close();
	
	void start();

}
