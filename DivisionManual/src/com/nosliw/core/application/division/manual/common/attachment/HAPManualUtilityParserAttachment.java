package com.nosliw.core.application.division.manual.common.attachment;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperBrickRoot;

public class HAPManualUtilityParserAttachment{

	public static HAPManualDefinitionAttachment parseAttachmentJson(Object jsonValue, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualBrickMan) {
		HAPManualDefinitionAttachment attachment = new HAPManualDefinitionAttachment();

		JSONObject jsonObj = (JSONObject)jsonValue;
		for(Object typeKey : jsonObj.keySet()) {
			String brickType = (String)typeKey;
			JSONObject byVersionJsonObj = jsonObj.getJSONObject(brickType);
			for(Object versionKey : byVersionJsonObj.keySet()) {
				String brickVersion = (String)versionKey;
				JSONArray brickWrapperJsonArray = byVersionJsonObj.getJSONArray(brickVersion);
				for(int i=0; i<brickWrapperJsonArray.length(); i++) {
					HAPManualDefinitionWrapperBrickRoot brickWrapper = manualBrickMan.parseBrickDefinitionWrapper(brickWrapperJsonArray.get(i), new HAPIdBrickType(brickType, brickVersion), HAPSerializationFormat.JSON, parseContext);
					attachment.addItem(brickWrapper);
				}
			}
		}
		return attachment;
	}
}
