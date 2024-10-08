package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.scriptexpression.library.HAPBlockScriptExpressionElementInLibrary;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualBlockScriptExpressionElementInLibrary extends HAPManualDefinitionBrick{

	public HAPManualBlockScriptExpressionElementInLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100);
	}

	public HAPManualScriptExpressionLibraryElement getValue() {   return (HAPManualScriptExpressionLibraryElement)this.getAttributeValueOfValue(HAPBlockScriptExpressionElementInLibrary.VALUE);      }
	public void setValue(HAPManualScriptExpressionLibraryElement value) {     this.setAttributeValueWithValue(HAPBlockScriptExpressionElementInLibrary.VALUE, value);      }
	
}
