package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPUnitDataExpression {

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	HAPContainerVariableCriteriaInfo getVariablesInfo();

}
