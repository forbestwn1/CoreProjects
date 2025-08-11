package com.nosliw.core.xxx.application.common.scriptexpression;

import java.util.Map;

import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionContainerScriptExpression;

public interface HAPWithScriptExpressionConstant {

	void discoverConstantScript(HAPDefinitionContainerScriptExpression scriptExpressionContainer);

	void solidateConstantScript(Map<String, Object> values);
	
}
