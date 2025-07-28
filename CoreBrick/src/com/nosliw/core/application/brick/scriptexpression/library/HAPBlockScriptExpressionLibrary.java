package com.nosliw.core.application.brick.scriptexpression.library;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.division.manual.executable.HAPBrickBlockSimple;

@HAPEntityWithAttribute
public class HAPBlockScriptExpressionLibrary extends HAPBrickBlockSimple{

	@HAPAttribute
	public static String ITEM = "item";


	@Override
	public void init() {	
		this.setAttributeValueWithBrick(ITEM, new HAPBrickContainer());
	}

	public HAPBrickContainer getItems() {
		return (HAPBrickContainer)this.getAttributeValueOfBrickLocal(ITEM);
	}

}
