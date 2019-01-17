package com.nosliw.data.core.service;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;

public class HAPUtilityService {

	public static HAPResultService generateSuccessResult(HAPData dataOutput) {
		Map<String, HAPData> output = new LinkedHashMap<String, HAPData>();
		output.put(HAPConstant.SERVICE_OUTPUTNAME_OUTPUT, dataOutput);
		return new HAPResultService(HAPConstant.SERVICE_RESULT_SUCCESS, output);
	}
	
}
