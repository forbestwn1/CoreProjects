package com.nosliw.core.application.common.interactive;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithInteractiveExpression {

	@HAPAttribute
	public static String EXPRESSIONINTERACTIVE = "expressionInteractive";

	HAPInteractiveExpression getExpressionInteractive();
	
}
