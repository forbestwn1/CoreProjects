package com.nosliw.core.application.division.manual.core.definition;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualDefinitionUtilityParserAttachment{

	public static HAPManualDefinitionAttachment parseAttachmentJson(Object jsonValue, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		HAPManualDefinitionAttachment attachment = new HAPManualDefinitionAttachment();

		JSONObject jsonObj = (JSONObject)jsonValue;
		for(Object typeKey : jsonObj.keySet()) {
			String brickType = (String)typeKey;
			JSONObject byVersionJsonObj = jsonObj.getJSONObject(brickType);
			for(Object versionKey : byVersionJsonObj.keySet()) {
				String brickVersion = (String)versionKey;
				JSONArray brickWrapperJsonArray = byVersionJsonObj.getJSONArray(brickVersion);
				for(int i=0; i<brickWrapperJsonArray.length(); i++) {
					HAPManualDefinitionWrapperBrickRoot brickWrapper = HAPManualDefinitionUtilityParserBrick.parseBrickDefinitionWrapper(brickWrapperJsonArray.get(i), new HAPIdBrickType(brickType, brickVersion), HAPSerializationFormat.JSON, parseContext, manualBrickMan, brickMan);
					attachment.addItem(brickWrapper);
				}
			}
		}
		return attachment;
	}
}
