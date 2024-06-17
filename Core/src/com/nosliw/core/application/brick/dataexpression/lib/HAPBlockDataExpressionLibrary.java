package com.nosliw.core.application.brick.dataexpression.lib;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.brick.container.HAPBrickContainer;

@HAPEntityWithAttribute
public class HAPBlockDataExpressionLibrary extends HAPBrickBlockSimple{

	@HAPAttribute
	public static String ITEM = "item";


	@Override
	public void init() {	
		this.setAttributeValueWithBrick(ITEM, new HAPBrickContainer());
	}

	public HAPBrickContainer getItems() {
		return (HAPBrickContainer)this.getAttributeValueOfBrick(ITEM);
	}

}
