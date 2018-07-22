package com.nosliw.data.core.runtime;

import java.util.List;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.HAPOperationParm;

public interface HAPRuntime {

	public static final boolean isDemo = false;
	
	//async request
	void executeTask(HAPRuntimeTask task);

	//sync request
	HAPServiceData executeTaskSync(HAPRuntimeTask task);

	//sync request
	//individual expression execute
	public HAPServiceData executeExpressionSync(String expressionStr, Map<String, HAPData> parmsData);

	//sync request
	//individual data operation execute
	public HAPServiceData executeDataOperationSync(HAPDataTypeId dataTypeId, String operation, List<HAPOperationParm> parmsData);
	
	void close();
	
	void start();

}
