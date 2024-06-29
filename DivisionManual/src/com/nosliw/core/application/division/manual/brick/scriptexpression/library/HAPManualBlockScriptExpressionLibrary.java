package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.scriptexpression.library.HAPBlockScriptExpressionLibrary;
import com.nosliw.core.application.division.manual.HAPManualAttribute;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualWrapperValue;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainer;

public class HAPManualBlockScriptExpressionLibrary extends HAPManualBrickBlockSimple{

	public HAPManualBlockScriptExpressionLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeWithValueBrick(HAPBlockScriptExpressionLibrary.ITEM, this.getManualBrickManager().newBrick(HAPEnumBrickType.CONTAINER_100));
	}

	public String addElement(HAPManualBlockScriptExpressionElementInLibrary element) {
		return this.getContainer().addElement(element);
	}
	
	public String addElement(HAPManualWrapperValue elementValueWrapper) {
		return this.getContainer().addElement(elementValueWrapper);
	}
	
	public String addElement(HAPManualAttribute element) {
		return this.getContainer().addElement(element);
	}
	
	private HAPManualBrickContainer getContainer() {
		return (HAPManualBrickContainer)this.getAttributeValueWithBrick(HAPBlockScriptExpressionLibrary.ITEM);
	}
}
