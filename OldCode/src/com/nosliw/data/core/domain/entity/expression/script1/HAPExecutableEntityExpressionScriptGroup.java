package com.nosliw.data.core.domain.entity.expression.script1;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScriptGroup extends HAPExecutableEntityExpressionScript{
	
	@HAPAttribute
	public static String EXPRESSIONS = "expressions";
	
	public HAPExecutableEntityExpressionScriptGroup() {
		this.setAttributeValueObject(EXPRESSIONS, new HAPExecutableContainerExpression());
	}

	public HAPExecutableContainerExpression getExprssionContainer() {   return (HAPExecutableContainerExpression)this.getAttributeValue(EXPRESSIONS);  }
	
	public List<HAPExecutableExpression> getAllExpressionItems(){   return this.getExprssionContainer().getAllExpressionItems();  }
	public void addExpressionItem(HAPExecutableExpression expressionItem) {    this.getExprssionContainer().addExpressionItem(expressionItem);       }
}
