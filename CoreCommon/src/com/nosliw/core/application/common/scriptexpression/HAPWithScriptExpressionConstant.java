package com.nosliw.core.application.common.scriptexpression;

import java.util.Map;

public interface HAPWithScriptExpressionConstant {

	void discoverConstantScript(HAPDefinitionContainerScriptExpression scriptExpressionContainer);

	void solidateConstantScript(Map<String, Object> values);
	
}
