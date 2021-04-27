package com.nosliw.data.core.structure.data;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.HAPUtilityData;

public class HAPContextDataFactory {

	public static HAPContextDataFlat newContextDataFlat(Map<String, HAPData> data) {
		return new HAPContextDataFlat(data);
	}
	
	public static HAPContextDataFlat newContextDataFlat(JSONObject dataMapJson) {
		return newContextDataFlat(HAPUtilityData.buildDataWrapperMapFromJson(dataMapJson));
	}
}
