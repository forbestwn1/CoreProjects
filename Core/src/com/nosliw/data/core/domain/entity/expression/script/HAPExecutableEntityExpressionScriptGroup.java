package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableExpressionData;

@HAPEntityWithAttribute
public class HAPExecutableEntityExpressionScriptGroup extends HAPExecutableEntityExpressionScript{
	
	@HAPAttribute
	public static String EXPRESSIONS = "expressions";
	
	public HAPExecutableEntityExpressionScriptGroup() {
		this.setAttributeValueObject(EXPRESSIONS, new ArrayList<HAPExecutableExpressionData>());
	}

	public List<HAPExecutableExpression> getAllExpressionItems(){   return (List<HAPExecutableExpression>)this.getAttributeValue(EXPRESSIONS);  }
	public void addExpressionItem(HAPExecutableExpression expressionItem) {    this.getAllExpressionItems().add(expressionItem);       }
}
