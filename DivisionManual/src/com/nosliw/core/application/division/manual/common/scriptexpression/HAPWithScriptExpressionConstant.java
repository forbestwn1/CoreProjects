package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;

public interface HAPWithScriptExpressionConstant {

	HAPManualDefinitionContainerScriptExpression getScriptExpressionConstantContainer();

	void discoverConstantScript();

	void solidateConstantScript(Map<String, String> values);
	
}
