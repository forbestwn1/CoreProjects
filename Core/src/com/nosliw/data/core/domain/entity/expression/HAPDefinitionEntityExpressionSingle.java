package com.nosliw.data.core.domain.entity.expression;

import java.util.List;

import com.google.common.collect.Lists;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;

//normal expression
public class HAPDefinitionEntityExpressionSingle extends HAPDefinitionEntityExpression{

	public static final String ATTR_EXPRESSION = "expression";

	public HAPDefinitionEntityExpressionSingle() {
	}
	
	public void setExpression(HAPDefinitionExpression expression) {	this.setNormalAttributeValueObject(ATTR_EXPRESSION, expression);	}

	public HAPDefinitionExpression getExpression() {    return (HAPDefinitionExpression)this.getNormalAttributeValue(ATTR_EXPRESSION);      }

	@Override
	public List<HAPDefinitionExpression> getAllExpressions(){   return Lists.newArrayList(this.getExpression());      }

	@Override
	public HAPDefinitionEntityInDomain cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionSingle out = new HAPDefinitionEntityExpressionSingle();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
