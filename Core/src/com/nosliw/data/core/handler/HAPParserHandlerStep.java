package com.nosliw.data.core.handler;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPParserHandlerStep {

	public static HAPHandlerStep parse(JSONObject obj) {
		HAPHandlerStep out = null;
		String stepType = obj.getString(HAPHandlerStep.STEPTYPE);
		if(stepType.equals(HAPConstant.HANDLERSTEP_TYPE_PROCESS)) {
			out = new HAPHandlerStepProcess();
		}
		else if(stepType.equals(HAPConstant.HANDLERSTEP_TYPE_ACTIVITY)) {
			out = new HAPHandlerStepActivity();
		}
		else if(stepType.equals(HAPConstant.HANDLERSTEP_TYPE_DATAASSOCIATION)) {
			out = new HAPHandlerStepDataAssociation();
		}
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}
	
}
