package com.nosliw.data.core.service.definition;

import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.data.HAPData;

public abstract class HAPExecutableServiceImp implements HAPExecutableService{

	protected void printParms(Map<String, HAPData> parms) {
		System.out.println(HAPJsonUtility.formatJson(HAPSerializeManager.getInstance().toStringValue(parms, HAPSerializationFormat.JSON)));
	}
	
	protected void printOutput(Map<String, HAPData> output) {
		System.out.println(HAPJsonUtility.formatJson(HAPSerializeManager.getInstance().toStringValue(output, HAPSerializationFormat.JSON)));
	}
	
}
