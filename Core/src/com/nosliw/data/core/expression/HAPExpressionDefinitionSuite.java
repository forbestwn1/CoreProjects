package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.HAPData;

/**
 * A group of expression definitions 
 */
@HAPEntityWithAttribute
public interface HAPExpressionDefinitionSuite {

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String EXPRESSIONDEFINITIONS = "expressionDefinitions";

	@HAPAttribute
	public static String CONSTANTS = "constants";
	
	//suite name
	String getName();
	
	//get expression definition by name
	HAPExpressionDefinition getExpressionDefinition(String name);

	//get all expression definitions in suite
	Map<String, HAPExpressionDefinition> getAllExpressionDefinitions();

	//get global constants in suite. these constants is visible to all expression definition in suite
	Map<String, HAPData> getConstants();
}
