package com.nosliw.data.core.expression;

import java.util.Map;

/**
 * The information used to define reference in expression
 * 		reference : the name of expression
 * 		variableMap: the mapping from variable in parent expression to variable in referenced expression 
 */
public interface HAPReferenceInfo{

	String getReference();
	
	Map<String, String> getVariableMap();
	
}
