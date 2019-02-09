package com.nosliw.data.core.service.provide;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;

public class HAPUtilityService {

	public static HAPResultService generateSuccessResult(Map<String, HAPData> output) {
		return new HAPResultService(HAPConstant.SERVICE_RESULT_SUCCESS, output);
	}
	
}
