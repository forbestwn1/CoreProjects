package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.scriptexpressio.HAPManualExpressionScript;
import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionScriptExpression;

public class HAPUtilityScriptExpressionConstant {

	private static String SYMBLE_IDLITERATE_START = "{{"; 
	private static String SYMBLE_IDLITERATE_END = "}}"; 
	
	public static String makeIdLiterate(String id) {
		return SYMBLE_IDLITERATE_START + id + SYMBLE_IDLITERATE_END;
	}
	
	public static String isIdLterate(String content) {
		String out = null;
		
		if(content!=null && content.startsWith(SYMBLE_IDLITERATE_START) && content.endsWith(SYMBLE_IDLITERATE_END)) {
			out = content.substring(SYMBLE_IDLITERATE_START.length(), content.length()-SYMBLE_IDLITERATE_END.length());
		}
		return out;
	}
	
	public static HAPManualExpressionScript processScriptExpressionConstant(HAPDefinitionScriptExpression scriptExpressionDef, Map<String, HAPDefinitionConstant> constantsDef, HAPParserDataExpression dataExpressionParser) {
		
		HAPManualExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(scriptExpressionDef.getScriptExpression(), scriptExpressionDef.getScriptExpressionType(), dataExpressionParser);

		HAPManualUtilityScriptExpression.processScriptExpressionConstant(scriptExpression, constantsDef);

		return scriptExpression;
	}


	
}
