package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockDataExpressionElementInLibrary extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockDataExpressionElementInLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100);
	}

	public HAPManualDataExpressionLibraryElement getValue() {   return (HAPManualDataExpressionLibraryElement)this.getAttributeValueWithValue(HAPBlockDataExpressionElementInLibrary.VALUE);      }
	public void setValue(HAPManualDataExpressionLibraryElement value) {     this.setAttributeWithValueValue(HAPBlockDataExpressionElementInLibrary.VALUE, value);      }
	
}
