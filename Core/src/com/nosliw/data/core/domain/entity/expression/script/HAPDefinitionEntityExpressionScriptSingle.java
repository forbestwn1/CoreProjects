package com.nosliw.data.core.domain.entity.expression.script;

import java.util.List;

import com.google.common.collect.Lists;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

//normal expression
public class HAPDefinitionEntityExpressionScriptSingle extends HAPDefinitionEntityExpressionScript{

	public static final String ATTR_EXPRESSION = "expression";

	public HAPDefinitionEntityExpressionScriptSingle() {
	}
	
	public void setExpression(HAPDefinitionExpression expression) {	this.setAttributeValueObject(ATTR_EXPRESSION, expression);	}

	public HAPDefinitionExpression getExpression() {    return (HAPDefinitionExpression)this.getAttributeValue(ATTR_EXPRESSION);      }

	@Override
	public List<HAPDefinitionExpression> getAllExpressionItems(){   return Lists.newArrayList(this.getExpression());      }

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionScriptSingle out = new HAPDefinitionEntityExpressionScriptSingle();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}