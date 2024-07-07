package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithVariable {

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	Map<String, HAPIdElement> getVariablesInfo();

}
