package com.nosliw.core.application.division.manual.brick.dataexpression.standalone;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.standalone.HAPBlockDataExpressionStandAlone;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpressionStandAlone;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockDataExpressionStandAlone extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockDataExpressionStandAlone() {
		super(HAPEnumBrickType.DATAEXPRESSIONSTANDALONE_100);
	}

	public HAPDefinitionDataExpressionStandAlone getValue() {   return (HAPDefinitionDataExpressionStandAlone)this.getAttributeValueOfValue(HAPBlockDataExpressionStandAlone.VALUE);      }
	public void setValue(HAPDefinitionDataExpressionStandAlone value) {     this.setAttributeValueWithValue(HAPBlockDataExpressionStandAlone.VALUE, value);      }
	
}
