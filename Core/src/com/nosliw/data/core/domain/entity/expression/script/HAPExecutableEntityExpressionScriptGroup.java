package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableExpressionData;

public class HAPExecutableEntityExpressionScriptGroup extends HAPExecutableEntityComplex{
	
	@HAPAttribute
	public static String EXPRESSIONS = "expressions";
	
	public HAPExecutableEntityExpressionScriptGroup() {
		this.setAttributeValueObject(EXPRESSIONS, new ArrayList<HAPExecutableExpressionData>());
	}

	public List<HAPExecutableExpression> getAllExpressionItems(){   return (List<HAPExecutableExpression>)this.getAttributeValue(EXPRESSIONS);  }
	public void addExpressionItem(HAPExecutableExpression expressionItem) {    this.getAllExpressionItems().add(expressionItem);       }

}
