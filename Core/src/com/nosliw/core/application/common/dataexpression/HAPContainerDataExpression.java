package com.nosliw.core.application.common.dataexpression;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.container.HAPContainer;

@HAPEntityWithAttribute
public class HAPContainerDataExpression extends HAPContainer<HAPItemInContainerDataExpression>{

	public HAPContainerDataExpression() {
	}

	public String addDataExpression(HAPExpressionData dataExpression) {
		HAPItemInContainerDataExpression item = new HAPItemInContainerDataExpression(dataExpression);
		return this.addItem(item);
	}
	
}
