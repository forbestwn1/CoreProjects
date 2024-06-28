package com.nosliw.data.core.domain.entity.expression.script;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.common.scriptexpression.HAPExpressionScript;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScriptGroup extends HAPExecutableEntityExpressionScript{
	
	@HAPAttribute
	public static String EXPRESSIONS = "expressions";
	
	public HAPExecutableEntityExpressionScriptGroup() {
		this.setAttributeValueObject(EXPRESSIONS, new HAPExecutableContainerExpression());
	}

	public HAPExecutableContainerExpression getExprssionContainer() {   return (HAPExecutableContainerExpression)this.getAttributeValue(EXPRESSIONS);  }
	
	public List<HAPExpressionScript> getAllExpressionItems(){   return this.getExprssionContainer().getAllExpressionItems();  }
	public void addExpressionItem(HAPExpressionScript expressionItem) {    this.getExprssionContainer().addExpressionItem(expressionItem);       }
}
