package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.runtime.HAPExecutable;

public interface HAPExecutableScript extends HAPExecutable{

	@HAPAttribute
	public static String SCRIPTTYPE = "scriptType";

	@HAPAttribute
	public static String ID = "id";

	String getScriptType();

	String getId();

	Set<HAPVariableInfo> discoverVariablesInfo(HAPExecutableExpressionGroup expressionGroup);
	
	Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableExpressionGroup expressionGroup);
	void updateConstant(Map<String, Object> value);
	
	Set<String> discoverExpressionReference(HAPExecutableExpressionGroup expressionGroup);
}
