package com.nosliw.core.application.common.valueport;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithVariable {

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	HAPContainerVariableInfo getVariablesInfo();

}
