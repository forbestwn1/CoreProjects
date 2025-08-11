package com.nosliw.core.application.common.scriptexpressio;

import java.util.Map;

import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionContainerScriptExpression;

public interface HAPWithScriptExpressionConstantMaster {

	HAPDefinitionContainerScriptExpression getScriptExpressionConstantContainer();

	void discoverConstantScript();

	void solidateConstantScript(Map<String, Object> values);
	
}
