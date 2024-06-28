package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScriptSingle extends HAPExecutableEntityExpressionScript{
	
	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	public HAPExecutableEntityExpressionScriptSingle() {	}

	public HAPExpressionScript getExpression(){   return (HAPExpressionScript)this.getAttributeValue(EXPRESSION);  }
	public void setExpression(HAPExpressionScript expression) {    this.setAttributeValueObject(EXPRESSION, expression);       }

}
