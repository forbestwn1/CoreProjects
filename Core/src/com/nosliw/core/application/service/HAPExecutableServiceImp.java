package com.nosliw.core.application.service;

import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.data.HAPData;

public abstract class HAPExecutableServiceImp implements HAPExecutableService{

	protected void printParms(Map<String, HAPData> parms) {
		System.out.println(HAPUtilityJson.formatJson(HAPSerializeManager.getInstance().toStringValue(parms, HAPSerializationFormat.JSON)));
	}
	
	protected void printOutput(Map<String, HAPData> output) {
		System.out.println(HAPUtilityJson.formatJson(HAPSerializeManager.getInstance().toStringValue(output, HAPSerializationFormat.JSON)));
	}
	
}
