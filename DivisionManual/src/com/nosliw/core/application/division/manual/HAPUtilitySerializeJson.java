package com.nosliw.core.application.division.manual;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPUtilityBrickId;
import com.nosliw.core.application.division.m.HAPManualBrick;

public class HAPUtilitySerializeJson {

	public static HAPManualBrick buildBrick(JSONObject jsonObj, HAPIdBrickType entityTypeIfNotProvided, HAPManagerApplicationBrick brickMan) {
		HAPIdBrickType brickType = HAPUtilityBrickId.parseBrickTypeId(jsonObj.opt(HAPManualBrick.BRICKTYPE), entityTypeIfNotProvided, brickMan);
		HAPManualBrick out = brickMan.newBrickInstance(brickType);
		out.buildBrick(jsonObj, HAPSerializationFormat.JSON, brickMan);
		return out;
	}
	
}
