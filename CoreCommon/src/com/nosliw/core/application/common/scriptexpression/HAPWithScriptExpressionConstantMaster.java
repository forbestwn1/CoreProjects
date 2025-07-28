package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

public interface HAPWithScriptExpressionConstantMaster {

	HAPDefinitionContainerScriptExpression getScriptExpressionConstantContainer();

	void discoverConstantScript();

	void solidateConstantScript(Map<String, Object> values);
	
}
