package com.nosliw.core.application.brick.dataexpression.group;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.common.dataexpression.HAPGroupDataExpression;

@HAPEntityWithAttribute
public class HAPBlockDataExpressionGroup extends HAPBrickBlockComplex{

	@HAPAttribute
	public static String VALUE = "value";
	
	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VALUE, new HAPGroupDataExpression());;
	}
	
	public HAPGroupDataExpression getValue(){	return (HAPGroupDataExpression)this.getAttributeValueOfValue(VALUE);	}

}
