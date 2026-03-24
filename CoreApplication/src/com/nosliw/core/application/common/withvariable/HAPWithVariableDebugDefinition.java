package com.nosliw.core.application.common.withvariable;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithVariableDebugDefinition {

	@HAPAttribute
	public static String VARIABLE = "variable";

	List<String> getVariables();
	
}
