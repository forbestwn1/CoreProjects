package com.nosliw.entity.expression;

import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.expression.HAPExpression;
import com.nosliw.expression.HAPExpressionInfo;

/*
 * an expression which is based on entity attribute, it has at lease predfined two variable
 *  	this:   entity attribute value
 *  	entity : the entity own this attribute
 */
public class HAPAttributeExpression extends HAPExpression{

	private HAPAttributeDefinition m_attrDef;
	
	public HAPAttributeExpression(HAPExpressionInfo expression, HAPAttributeDefinition attrDef, HAPDataTypeManager dataTypeMan) {
		super(HAPAttributeExpressionUtility.appendAttributeVarInfoToExpressionInfo(expression, attrDef), dataTypeMan);
		this.m_attrDef = attrDef;
	}

}
