package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;

public interface HAPRuntime {

	//async request
	void executeTask(HAPRuntimeTask task);

	//sync request
	HAPServiceData executeTaskSync(HAPRuntimeTask task);

	//sync request
	//individual expression execute
	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData);

	//sync request
	//individual data operation execute
	public HAPServiceData executeDataOperationSync(HAPDataTypeId dataTypeId, String operation, Map<String, HAPData> parmsData);
	
	void close();
	
	void start();

}
