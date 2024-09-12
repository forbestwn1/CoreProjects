package com.nosliw.data.core.script.expression1;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.data.core.domain.entity.expression.data1.HAPExecutableEntityExpressionDataGroup;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.valuestructure1.HAPVariableInfoInStructure;

public interface HAPExecutableScript extends HAPExecutable{

	@HAPAttribute
	public static String SCRIPTTYPE = "scriptType";

	@HAPAttribute
	public static String ID = "id";

	String getScriptType();

	String getId();

	HAPVariableInfoInStructure discoverVariablesInfo1(HAPExecutableEntityExpressionDataGroup expressionGroup);

	Set<String> discoverVariables(HAPExecutableEntityExpressionDataGroup expressionGroup);

	Set<HAPDefinitionConstant> discoverConstantsDefinition(HAPExecutableEntityExpressionDataGroup expressionGroup);
	void updateConstant(Map<String, Object> value);

	void updateVariableName(HAPUpdateName nameUpdate);
	
	Set<String> discoverExpressionReference(HAPExecutableEntityExpressionDataGroup expressionGroup);
}
