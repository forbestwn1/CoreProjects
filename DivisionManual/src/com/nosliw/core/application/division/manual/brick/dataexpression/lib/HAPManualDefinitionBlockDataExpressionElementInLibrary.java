package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrickWithEntityInfo;

public class HAPManualDefinitionBlockDataExpressionElementInLibrary extends HAPManualDefinitionBrickWithEntityInfo{

	public HAPManualDefinitionBlockDataExpressionElementInLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100);
	}

	public HAPManualDataExpressionLibraryElement getValue() {   return (HAPManualDataExpressionLibraryElement)this.getAttributeValueOfValue(HAPBlockDataExpressionElementInLibrary.VALUE);      }
	public void setValue(HAPManualDataExpressionLibraryElement value) {     this.setAttributeValueWithValue(HAPBlockDataExpressionElementInLibrary.VALUE, value);      }
	
}
