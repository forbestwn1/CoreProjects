package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.valuestructure.HAPContainerVariableCriteriaInfo;

public interface HAPExecutableScript extends HAPExecutable{

	@HAPAttribute
	public static String SCRIPTTYPE = "scriptType";

	@HAPAttribute
	public static String ID = "id";

	String getScriptType();

	String getId();

	HAPContainerVariableCriteriaInfo discoverVariablesInfo1(HAPExecutableExpressionGroup expressionGroup);

	Set<String> discoverVariables(HAPExecutableExpressionGroup expressionGroup);

	Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableExpressionGroup expressionGroup);
	void updateConstant(Map<String, Object> value);
	
	Set<String> discoverExpressionReference(HAPExecutableExpressionGroup expressionGroup);
}
