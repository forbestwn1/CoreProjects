package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionLibrary;
import com.nosliw.core.application.division.manual.brick.container.HAPManualBrickContainer;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionWrapperValue;

public class HAPManualBlockDataExpressionLibrary extends HAPManualDefinitionBrickBlockSimple{

	public HAPManualBlockDataExpressionLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeWithValueBrick(HAPBlockDataExpressionLibrary.ITEM, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}

	public String addElement(HAPManualBlockDataExpressionElementInLibrary element) {
		return this.getContainer().addElement(element);
	}
	
	public String addElement(HAPManualDefinitionWrapperValue elementValueWrapper) {
		return this.getContainer().addElement(elementValueWrapper);
	}
	
	public String addElement(HAPManualDefinitionAttributeInBrick element) {
		return this.getContainer().addElement(element);
	}
	
	private HAPManualBrickContainer getContainer() {
		return (HAPManualBrickContainer)this.getAttributeValueWithBrick(HAPBlockDataExpressionLibrary.ITEM);
	}
}
