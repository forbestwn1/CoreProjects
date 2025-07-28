package com.nosliw.data.core.cronjob;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPManagerScheduleDefinition {

	public HAPExecutablePollSchedule parsePollSchedule(JSONObject json) {
		HAPExecutablePollSchedule out = null;
		String type = json.getString(HAPExecutablePollSchedule.TYPE);
		if(type.equals(HAPExecutablePollScheduleImpNormal.TYPE)) {
			out = new HAPExecutablePollScheduleImpNormal();
			((HAPExecutablePollScheduleImpNormal)out).buildObject(json, HAPSerializationFormat.JSON);
		}
		
		return out;
	}
	
	
}
