package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.Map;

public interface HAPWithScriptExpressionConstant {

	void discoverConstantScript(HAPManualDefinitionContainerScriptExpression scriptExpressionContainer);

	void solidateConstantScript(Map<String, Object> values);
	
}
