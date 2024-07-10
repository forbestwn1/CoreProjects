package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockDataExpressionLibrary extends HAPManualBrickImp implements HAPBlockDataExpressionLibrary{

	@Override
	public void init() {	
	}

	@Override
	public HAPBrickContainer getItems() {
		return (HAPBrickContainer)this.getAttributeValueOfBrick(ITEM);
	}
}
