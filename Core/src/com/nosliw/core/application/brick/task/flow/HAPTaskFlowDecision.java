package com.nosliw.core.application.brick.task.flow;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public interface HAPTaskFlowDecision extends HAPSerializable{

	@HAPAttribute
	public static final String TYPE = "type";

	String getType();
	
	public static HAPTaskFlowDecision parseDecision(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;

		String type = jsonObj.getString(TYPE);
		if(type.equals(HAPConstantShared.FLOW_DECISION_TYPE_JAVASCRIPT)) {
			HAPTaskFlowDecisionJS jsDecision = new HAPTaskFlowDecisionJS();
			jsDecision.buildObject(jsonObj, HAPSerializationFormat.JSON);
		}
		return null;
	}
	
}
