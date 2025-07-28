package com.nosliw.data.core.process1;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.data.HAPData;

public interface HAPProcessResultHandler {

	void onSuccess(String resultName, Map<String, HAPData> resultData);
	
	void onError(HAPServiceData serviceData);
	
}
