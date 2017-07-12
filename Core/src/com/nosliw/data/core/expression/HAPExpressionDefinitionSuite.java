package com.nosliw.data.core.expression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPExpressionDefinitionSuite {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String EXPRESSIONDEFINITIONS = "expressionDefinitions";

	String getName();
	
	HAPExpressionDefinition getExpressionDefinition(String name);
	
}
