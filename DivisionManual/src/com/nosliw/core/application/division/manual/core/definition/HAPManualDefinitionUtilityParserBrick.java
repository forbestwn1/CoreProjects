package com.nosliw.core.application.division.manual.core.definition;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.HAPIdBrickType;

public class HAPManualDefinitionUtilityParserBrick {

	public static HAPManualDefinitionBrick parseBrickDefinition(Object entityObj, HAPIdBrickType brickTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionPluginParserBrick entityParserPlugin = parseContext.getManualBrickManager().getBrickParsePlugin(brickTypeId);
		return entityParserPlugin.parse(entityObj, format, parseContext);
	}

	public static HAPManualDefinitionWrapperBrickRoot parseBrickDefinitionWrapper(Object entityObj, HAPIdBrickType brickTypeId, HAPSerializationFormat format, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionWrapperBrickRoot out = null;
		switch(format) {
		case JSON:
			out = HAPManualDefinitionUtilityParserBrickFormatJson.parseRootBrickWrapper((JSONObject)HAPUtilityJson.toJsonObject(entityObj), brickTypeId, parseContext);
			break;
		case HTML:
			out = new HAPManualDefinitionWrapperBrickRoot(parseBrickDefinition(entityObj, brickTypeId, HAPSerializationFormat.HTML, parseContext));
			break;
		case JAVASCRIPT:
			break;
		default:
		}
		return out;
	}

}
