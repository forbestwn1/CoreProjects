package com.nosliw.core.application.division.manual.brick.expression.dataexpression.lib;

import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.brick.expression.dataexpression.library.HAPBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualBlockDataExpressionLibrary extends HAPManualBrickImp implements HAPBlockDataExpressionLibrary{

	@Override
	public void init() {	
		super.init();
	}

	@Override
	public HAPBrickContainer getItems() {
		return (HAPBrickContainer)this.getAttributeValueOfBrickLocal(ITEM);
	}
}
