package com.nosliw.data.core.script.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.data.core.common.HAPDefinitionConstant;
import com.nosliw.data.core.expression.HAPExecutableExpressionGroup;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.valuestructure.HAPVariableInfoInStructure;

public interface HAPExecutableScript extends HAPExecutable{

	@HAPAttribute
	public static String SCRIPTTYPE = "scriptType";

	@HAPAttribute
	public static String ID = "id";

	String getScriptType();

	String getId();

	HAPVariableInfoInStructure discoverVariablesInfo1(HAPExecutableExpressionGroup expressionGroup);

	Set<String> discoverVariables(HAPExecutableExpressionGroup expressionGroup);

	Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableExpressionGroup expressionGroup);
	void updateConstant(Map<String, Object> value);

	void updateVariableName(HAPUpdateName nameUpdate);
	
	Set<String> discoverExpressionReference(HAPExecutableExpressionGroup expressionGroup);
}
