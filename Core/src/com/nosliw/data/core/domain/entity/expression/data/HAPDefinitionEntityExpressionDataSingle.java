package com.nosliw.data.core.domain.entity.expression.data;

import java.util.List;

import com.google.common.collect.Lists;
import com.nosliw.core.application.division.manual.HAPManualEntity;

//normal expression
public class HAPDefinitionEntityExpressionDataSingle extends HAPDefinitionEntityExpressionData{

	public static final String ATTR_EXPRESSION = "expression";

	public final static String ATTR_TYPE = "type";

	public HAPDefinitionEntityExpressionDataSingle() {
	}
	
	public void setExpression(HAPDefinitionExpressionData expression) {	this.setAttributeValueObject(ATTR_EXPRESSION, expression);	}

	public HAPDefinitionExpressionData getExpression() {    return (HAPDefinitionExpressionData)this.getAttributeValue(ATTR_EXPRESSION);      }

	@Override
	public List<HAPDefinitionExpressionData> getAllExpressionItems(){   return Lists.newArrayList(this.getExpression());      }

	@Override
	public HAPManualEntity cloneEntityDefinitionInDomain() {
		HAPDefinitionEntityExpressionDataSingle out = new HAPDefinitionEntityExpressionDataSingle();
		this.cloneToDefinitionEntityInDomain(out);
		return out;
	}
}
