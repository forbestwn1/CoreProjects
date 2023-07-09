package com.nosliw.data.core.domain.entity.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.operand.HAPContainerVariableCriteriaInfo;

@HAPEntityWithAttribute(baseName="EXPRESSION")
public abstract class HAPExecutableEntityExpression extends HAPExecutableEntityComplex{

	@HAPAttribute
	public static String VARIABLEINFOS = "variableInfos";

	public static final String ATTRIBUTESREFERENCE = "referenceAttribute";
	
	public abstract List<HAPExecutableExpression> getAllExpressionItems();

	public HAPExecutableEntityExpression() {
		this.setNormalAttributeValueObject(ATTRIBUTESREFERENCE, new HashSet<String>());
	}

	public void setVariablesInfo(HAPContainerVariableCriteriaInfo varInfo) {  this.setNormalAttributeValueObject(VARIABLEINFOS, varInfo);  }
	public HAPContainerVariableCriteriaInfo getVariablesInfo() {   return (HAPContainerVariableCriteriaInfo)this.getNormalAttributeValue(VARIABLEINFOS);    }
	
	public Set<String> getReferenceAttributes(){    return (Set<String>)this.getNormalAttributeValue(ATTRIBUTESREFERENCE);     }
	
}
