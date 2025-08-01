package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.scriptexpression.library.HAPBlockScriptExpressionLibrary;

public class HAPManualBlockScriptExpressionLibrary extends HAPManualDefinitionBrick{

	public HAPManualBlockScriptExpressionLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeValueWithBrick(HAPBlockScriptExpressionLibrary.ITEM, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}

	public String addElement(HAPManualBlockScriptExpressionElementInLibrary element) {
		return this.getContainer().addElementWithBrickOrReference(element);
	}
	
	public String addElement(HAPManualDefinitionAttributeInBrick element) {
		return this.getContainer().addElementWithAttribute(element);
	}
	
	private HAPManualDefinitionBrickContainer getContainer() {
		return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockScriptExpressionLibrary.ITEM);
	}
}
