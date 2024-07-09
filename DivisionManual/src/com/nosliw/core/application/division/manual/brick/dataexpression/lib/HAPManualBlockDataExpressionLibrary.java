package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPManualBlockDataExpressionLibrary extends HAPManualBrickBlockSimple implements HAPBlockDataExpressionLibrary{

	@Override
	public void init() {	
//		this.setAttributeValueWithBrick(ITEM, new HAPBrickContainer());
	}

	@Override
	public HAPBrickContainer getItems() {
		return (HAPBrickContainer)this.getAttributeValueOfBrick(ITEM);
	}
}
