package com.nosliw.core.application.division.manual.brick.scriptexpression.library;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.scriptexpression.library.HAPBlockScriptExpressionElementInLibrary;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;

public class HAPManualBlockScriptExpressionElementInLibrary extends HAPManualDefinitionBrickBlockSimple{

	public HAPManualBlockScriptExpressionElementInLibrary() {
		super(HAPEnumBrickType.SCRIPTEXPRESSIONLIBELEMENT_100);
	}

	public HAPManualScriptExpressionLibraryElement getValue() {   return (HAPManualScriptExpressionLibraryElement)this.getAttributeValueWithValue(HAPBlockScriptExpressionElementInLibrary.VALUE);      }
	public void setValue(HAPManualScriptExpressionLibraryElement value) {     this.setAttributeWithValueValue(HAPBlockScriptExpressionElementInLibrary.VALUE, value);      }
	
}
