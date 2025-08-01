package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.container.HAPBrickContainer;
import com.nosliw.core.xxx.application1.brick.dataexpression.library.HAPBlockDataExpressionLibrary;

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
