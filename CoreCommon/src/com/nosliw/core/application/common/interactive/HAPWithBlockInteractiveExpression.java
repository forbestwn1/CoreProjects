package com.nosliw.core.application.common.interactive;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;

public interface HAPWithBlockInteractiveExpression {

	@HAPAttribute
	public static String EXPRESSIONINTERFACE = "expressionInterface";
	
	public HAPEntityOrReference getExpressionInterface();

}
