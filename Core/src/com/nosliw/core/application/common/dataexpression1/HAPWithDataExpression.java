package com.nosliw.core.application.common.dataexpression1;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithDataExpression {

	@HAPAttribute
	public static String DATAEXPRESSION = "dataExpression";

	HAPContainerDataExpression getDataExpressions();
	
	
}
