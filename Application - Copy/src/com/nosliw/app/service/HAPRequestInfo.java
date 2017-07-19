package com.nosliw.app.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.utils.HAPJsonUtility;

public class HAPRequestInfo implements HAPSerializable{

	public String requestId;
	
	public HAPRequestInfo(String reqId){
		this.requestId = reqId;
	}
	
	public HAPRequestInfo(){
	}
	
	@Override
	public String toStringValue(String format) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put("requestId", this.requestId);
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	public static HAPRequestInfo getRequestInfo(JSONObject jsonObj){
		HAPRequestInfo out = new HAPRequestInfo();
		out.requestId = jsonObj.optString("requestId", null);
		return out;
	}
}
