package com.nosliw.core.application.division.manual.core.definition;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualDefinitionUtilityParserBrick {

	public static HAPManualDefinitionBrick parseBrickDefinition(Object entityObj, HAPIdBrickType brickTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualBrickMan) {
		HAPManualDefinitionPluginParserBrick entityParserPlugin = manualBrickMan.getBrickParsePlugin(brickTypeId);
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	public static HAPManualDefinitionWrapperBrickRoot parseBrickDefinitionWrapper(Object entityObj, HAPIdBrickType brickTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext, HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		HAPManualDefinitionWrapperBrickRoot out = null;
		switch(format) {
		case JSON:
			out = HAPManualDefinitionUtilityParserBrickFormatJson.parseRootBrickWrapper((JSONObject)HAPUtilityJson.toJsonObject(entityObj), brickTypeId, parseContext, manualBrickMan, brickMan);
			break;
		case HTML:
			out = new HAPManualDefinitionWrapperBrickRoot(parseBrickDefinition(entityObj, brickTypeId, HAPSerializationFormat.HTML, parseContext, manualBrickMan));
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}

}
