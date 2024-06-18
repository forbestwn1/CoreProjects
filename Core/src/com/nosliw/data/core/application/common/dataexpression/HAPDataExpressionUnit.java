package com.nosliw.data.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPDataExpressionUnit {

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	HAPContainerVariableCriteriaInfo getVariablesInfo();

}
