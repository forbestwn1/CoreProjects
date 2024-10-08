package com.nosliw.core.application.brick.scriptexpression.group;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;

@HAPEntityWithAttribute
public interface HAPBlockScriptExpressionGroup extends HAPBrick{

	@HAPAttribute
	public static String VALUE = "value";
	
	HAPContainerScriptExpression getValue();
}
