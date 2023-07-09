package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

@HAPEntityWithAttribute(baseName="EXPRESSION")
public abstract class HAPExecutableEntityExpression extends HAPExecutableEntityComplex{

	public static final String ATTR_ATTRIBUTES_REFERENCE = "referenceAttribute";
	
	public abstract List<HAPExecutableExpression> getAllExpressionItems();

	public HAPExecutableEntityExpression() {
		this.setNormalAttributeValueObject(ATTR_ATTRIBUTES_REFERENCE, new HashSet<String>());
	}
	
	public Set<String> getReferenceAttributes(){    return (Set<String>)this.getNormalAttributeValue(ATTR_ATTRIBUTES_REFERENCE);     }
	
}
