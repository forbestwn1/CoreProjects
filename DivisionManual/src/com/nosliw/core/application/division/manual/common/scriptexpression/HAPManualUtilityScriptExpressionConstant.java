package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;

import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;

public class HAPManualUtilityScriptExpressionConstant {

	public static HAPManualExpressionScript processConstantScriptExpression(HAPManualDefinitionScriptExpressionConstant scriptExpressionDef, Map<String, HAPDefinitionConstant> constantsDef, HAPParserDataExpression dataExpressionParser) {
		
		HAPManualExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(scriptExpressionDef.getScriptExpression(), scriptExpressionDef.getScriptExpression(), dataExpressionParser);

		HAPManualUtilityScriptExpression.processScriptExpressionConstant(scriptExpression, constantsDef);

		return scriptExpression;
	}
	
}
