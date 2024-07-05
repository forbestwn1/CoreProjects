package com.nosliw.data.core.scriptexpression;

import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinitionComplex;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPUtilityScriptExpressionDefinition;

public class HAPUtilityScriptExpression {

	private static String SYMBLE_IDLITERATE_START = "{{"; 
	private static String SYMBLE_IDLITERATE_END = "}}"; 
	
	public static String discoverConstantScript(String content, HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser) {
		String out = null;
		if(HAPUtilityScriptExpressionDefinition.isScriptExpression(content)) {
			HAPDefinitionExpression expressionDef = HAPUtilityScriptExpressionDefinition.parseDefinitionExpressionLiterate(content, null, expressionParser);
			return HAPUtilityEntityDefinitionComplex.addPlainScriptExpressionToComplexEntity(expressionDef, complexEntityId, parserContext);
		}
		return out;
	}

	public static String makeIdLiterate(String id) {
		return SYMBLE_IDLITERATE_START + id + SYMBLE_IDLITERATE_END;
	}
	
	public static String isIdLterate(String content) {
		String out = null;
		if(content.startsWith(SYMBLE_IDLITERATE_START) && content.endsWith(SYMBLE_IDLITERATE_END)) {
			out = content.substring(SYMBLE_IDLITERATE_START.length(), content.length()-SYMBLE_IDLITERATE_END.length());
		}
		return out;
	}
}
