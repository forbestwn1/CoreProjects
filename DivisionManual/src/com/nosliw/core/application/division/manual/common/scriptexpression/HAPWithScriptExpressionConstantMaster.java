package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;

public interface HAPWithScriptExpressionConstantMaster {

	HAPManualDefinitionContainerScriptExpression getScriptExpressionConstantContainer();

	void discoverConstantScript();

	void solidateConstantScript(Map<String, Object> values);
	
}
