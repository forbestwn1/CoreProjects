package com.nosliw.data.core.domain.entity.expression.script1;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScriptSingle extends HAPExecutableEntityExpressionScript{
	
	@HAPAttribute
	public static String EXPRESSION = "expression";
	
	public HAPExecutableEntityExpressionScriptSingle() {	}

	public HAPExecutableExpression getExpression(){   return (HAPExecutableExpression)this.getAttributeValue(EXPRESSION);  }
	public void setExpression(HAPExecutableExpression expression) {    this.setAttributeValueObject(EXPRESSION, expression);       }

}
