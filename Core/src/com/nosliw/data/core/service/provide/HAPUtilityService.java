package com.nosliw.data.core.service.provide;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.HAPData;

public class HAPUtilityService {

	public static HAPResultService generateSuccessResult(Map<String, HAPData> output) {
		return new HAPResultService(HAPConstant.SERVICE_RESULT_SUCCESS, output);
	}

	public static Map<String, HAPResultService> readServiceResult(InputStream inputStream) {
		Map<String, HAPResultService> out = new LinkedHashMap<String, HAPResultService>();
		String content = HAPFileUtility.readFile(inputStream);
		JSONArray resultArray = new JSONArray(content);
		for(int i=0; i<resultArray.length(); i++) {
			HAPResultService result = new HAPResultService();
			result.buildObject(resultArray.getJSONObject(i), HAPSerializationFormat.JSON);
		}
		return out;
	}
	
	public static HAPResultService readServiceResult(InputStream inputStream, String resultName) {
		return readServiceResult(inputStream).get(resultName);
	}
}
