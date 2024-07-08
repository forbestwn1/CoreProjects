package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.scriptexpression.library.HAPBlockScriptExpressionLibrary;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainer;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;

public class HAPManualBlockScriptExpressionLibrary extends HAPManualDefinitionBrickBlockSimple{

	public HAPManualBlockScriptExpressionLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIB_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeWithValueBrick(HAPBlockScriptExpressionLibrary.ITEM, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}

	public String addElement(HAPManualBlockScriptExpressionElementInLibrary element) {
		return this.getContainer().addElement(element);
	}
	
	public String addElement(HAPManualDefinitionWrapperValue elementValueWrapper) {
		return this.getContainer().addElement(elementValueWrapper);
	}
	
	public String addElement(HAPManualDefinitionAttributeInBrick element) {
		return this.getContainer().addElement(element);
	}
	
	private HAPManualBrickContainer getContainer() {
		return (HAPManualBrickContainer)this.getAttributeValueWithBrick(HAPBlockScriptExpressionLibrary.ITEM);
	}
}
