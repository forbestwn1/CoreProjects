package com.nosliw.core.application.brick.dataexpression.group;

import java.util.ArrayList;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.common.dataexpression.HAPExecutableExpressionData1;

@HAPEntityWithAttribute
public class HAPBlockDataExpressions extends HAPBrickBlockComplex{

	@HAPAttribute
	public static String ITEMS = "items";


	@Override
	public void init() {	
		this.setAttributeValueWithValue(ITEMS, new ArrayList<HAPExecutableExpressionData1>());;
	}

	
	
}
