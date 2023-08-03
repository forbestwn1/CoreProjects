package com.nosliw.data.core.domain.entity.expression.script;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.expression.data.HAPExecutableExpressionData;

public class HAPExecuteEntityExpressionScriptGroup extends HAPExecutableEntityComplex{
	
	@HAPAttribute
	public static String EXPRESSIONS = "expressions";
	
	public HAPExecuteEntityExpressionScriptGroup() {
		this.setAttributeValueObject(EXPRESSIONS, new ArrayList<HAPExecutableExpressionData>());
	}

	public List<HAPExecuteExpression> getAllExpressionItems(){   return (List<HAPExecuteExpression>)this.getAttributeValue(EXPRESSIONS);  }
	public void addExpressionItem(HAPExecuteExpression expressionItem) {    this.getAllExpressionItems().add(expressionItem);       }

}
