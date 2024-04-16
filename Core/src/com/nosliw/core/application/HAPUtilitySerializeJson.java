package com.nosliw.core.application;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPUtilitySerializeJson {

	public static HAPBrick buildBrick(JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPManagerApplicationBrick brickMan) {
		HAPIdBrickType brickType = HAPUtilityBrick.parseBrickTypeId(jsonObj.opt(HAPBrick.BRICKTYPE), entityTypeIfNotProvided, brickMan);
		HAPBrick out = brickMan.getBrickPlugin(brickType).newInstance();
		out.buildBrick(jsonObj, HAPSerializationFormat.JSON, brickMan);
		return out;
	}
	
	
}
