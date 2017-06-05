package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;

/**
 * The information used to define reference in expression
 * 		reference : the name of expression
 * 		variableMap: the mapping from variable in parent expression to variable in referenced expression 
 */
public interface HAPReferenceInfo{

	@HAPAttribute
	public static String REFERENCE = "reference";
	
	@HAPAttribute
	public static String VARIABLESMAP = "variablesMap";
	
	
	String getReference();
	
	Map<String, String> getVariablesMap();
	
}
