package com.nosliw.data.core.scriptexpression;

import java.util.Map;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data1.HAPParserDataExpression;

public interface HAPWithConstantScriptExpression {

	void discoverConstantScript(HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext, HAPParserDataExpression expressionParser);

	void solidateConstantScript(Map<String, String> values);
	
}
