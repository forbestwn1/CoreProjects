package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.dataexpression.library.HAPBlockDataExpressionLibrary;

public class HAPManualDefinitionBlockDataExpressionLibrary extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockDataExpressionLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIB_100);
	}
	
	@Override
	protected void init() {
		this.setAttributeValueWithBrick(HAPBlockDataExpressionLibrary.ITEM, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}

	public String addElement(HAPManualDefinitionBlockDataExpressionElementInLibrary element) {
		return this.getContainer().addElementWithBrick(element);
	}
	
	private HAPManualDefinitionBrickContainer getContainer() {
		return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockDataExpressionLibrary.ITEM);
	}
}
