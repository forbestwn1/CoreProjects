package com.nosliw.data.core.scriptexpression;

import java.util.Map;

import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPWithConstantScriptExpression {

	void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser);

	void solidateConstantScript(Map<String, String> values);
	
}
