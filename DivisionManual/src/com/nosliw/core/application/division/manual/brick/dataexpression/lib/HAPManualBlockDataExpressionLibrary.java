package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainer;

public class HAPManualBlockDataExpressionLibrary extends HAPManualBrickBlockSimple{

	public HAPManualBlockDataExpressionLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100);
		
		this.setAttributeWithValueBrick(HAPBlockDataExpressionLibrary.ITEM, new HAPManualBrickContainer());
	}

	public String addElement(HAPManualBlockDataExpressionElementInLibrary element) {
		HAPManualBrickContainer container = (HAPManualBrickContainer)this.getAttributeValueWithBrick(HAPBlockDataExpressionLibrary.ITEM);
		return container.addElement(container);
	}
	
	public String addElement(HAPManualAttribute element) {
		HAPManualBrickContainer container = (HAPManualBrickContainer)this.getAttributeValueWithBrick(HAPBlockDataExpressionLibrary.ITEM);
		return container.addElement(element);
	}
}
