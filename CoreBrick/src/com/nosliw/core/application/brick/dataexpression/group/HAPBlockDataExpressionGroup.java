package com.nosliw.core.application.brick.dataexpression.group;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.dataexpression.HAPContainerDataExpression;

@HAPEntityWithAttribute
public interface HAPBlockDataExpressionGroup extends HAPBrick{

	@HAPAttribute
	public static String VALUE = "value";
	
	HAPContainerDataExpression getValue();

}
