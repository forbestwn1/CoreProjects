package com.nosliw.core.application.common.withvariable;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.valueport.HAPVariableInfo;

@HAPEntityWithAttribute
public interface HAPWithVariable {

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	@HAPAttribute
	public static String ENTITYTYPE = "entityType";

	Map<String, HAPVariableInfo> getVariablesInfo();
	void addVariableInfo(HAPVariableInfo variableInfo);

	String getWithVariableEntityType();
	
}
