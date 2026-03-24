package com.nosliw.core.application.common.withvariable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithVariableDebugExecutable {

	@HAPAttribute
	public static String VARIABLE = "variable";

	Map<String, HAPVariableInfo> getVariables();
	
}
