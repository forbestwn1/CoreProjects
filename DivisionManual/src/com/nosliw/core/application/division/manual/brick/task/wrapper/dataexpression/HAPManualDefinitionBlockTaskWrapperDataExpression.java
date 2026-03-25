package com.nosliw.core.application.division.manual.brick.task.wrapper.dataexpression;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.wrapper.dataexpression.HAPBlockTaskWrapperDataExpression;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionDataExpressionStandAlone;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockTaskWrapperDataExpression extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockTaskWrapperDataExpression() {
		super(HAPEnumBrickType.TASKWRAPPERDATAEXPRESSION);
	}

	public HAPDefinitionDataExpressionStandAlone getDataExpression() {   return (HAPDefinitionDataExpressionStandAlone)this.getAttributeValueOfValue(HAPBlockTaskWrapperDataExpression.DATAEXPRESSION);      }
	public void setDataExpression(HAPDefinitionDataExpressionStandAlone value) {     this.setAttributeValueWithValue(HAPBlockTaskWrapperDataExpression.DATAEXPRESSION, value);      }
	
}
