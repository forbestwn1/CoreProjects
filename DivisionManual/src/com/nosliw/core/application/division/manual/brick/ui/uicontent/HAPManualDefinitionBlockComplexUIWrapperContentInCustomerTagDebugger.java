package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUIWrapperContentInCustomerTagDebugger;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainerList;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger() {
		super(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100);
	}

	@Override
	public void init() {
		this.setAttributeValueWithBrick(HAPBlockComplexUIWrapperContentInCustomerTagDebugger.CHILD, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINERLIST_100));
	}

	public String addChild(HAPManualDefinitionBlockComplexUICustomerTagDebugger child) {	return this.getContainer().addElementWithBrick(child);	}
	private HAPManualDefinitionBrickContainerList getContainer() {	return (HAPManualDefinitionBrickContainerList)this.getAttributeValueOfBrick(HAPBlockComplexUIWrapperContentInCustomerTagDebugger.CHILD);	}

}
