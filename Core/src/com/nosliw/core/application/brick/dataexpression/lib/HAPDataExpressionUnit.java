package com.nosliw.core.application.brick.dataexpression.lib;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;

@HAPEntityWithAttribute
public interface HAPDataExpressionUnit {

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	HAPContainerVariableCriteriaInfo getVariablesInfo();

}
