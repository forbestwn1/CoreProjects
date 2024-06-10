package com.nosliw.core.application.brick.dataexpression.lib;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;

public interface HAPDataExpressionUnit {

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	HAPContainerVariableCriteriaInfo getVariablesInfo();

}
