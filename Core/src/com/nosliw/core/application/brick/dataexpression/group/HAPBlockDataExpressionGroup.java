package com.nosliw.core.application.brick.dataexpression.group;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.valueport.HAPWithVariable;

@HAPEntityWithAttribute
public interface HAPBlockDataExpressionGroup extends HAPBrick, HAPWithVariable{

	@HAPAttribute
	public static String VALUE = "value";
	
	HAPGroupDataExpression getValue();

}
