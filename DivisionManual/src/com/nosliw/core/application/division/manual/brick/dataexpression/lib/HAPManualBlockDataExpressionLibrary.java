package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualWrapperValue;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainer;

public class HAPManualBlockDataExpressionLibrary extends HAPManualBrickBlockSimple{

	public HAPManualBlockDataExpressionLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100);
		
		this.setAttributeWithValueBrick(HAPBlockDataExpressionLibrary.ITEM, new HAPManualBrickContainer());
	}

	public String addElement(HAPManualBlockDataExpressionElementInLibrary element) {
		return this.getContainer().addElement(element);
	}
	
	public String addElement(HAPManualWrapperValue elementValueWrapper) {
		return this.getContainer().addElement(elementValueWrapper);
	}
	
	public String addElement(HAPManualAttribute element) {
		return this.getContainer().addElement(element);
	}
	
	private HAPManualBrickContainer getContainer() {
		return (HAPManualBrickContainer)this.getAttributeValueWithBrick(HAPBlockDataExpressionLibrary.ITEM);
	}
}
