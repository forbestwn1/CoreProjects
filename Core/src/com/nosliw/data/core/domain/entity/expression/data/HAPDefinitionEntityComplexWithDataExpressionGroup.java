package com.nosliw.data.core.domain.entity.expression.data;

import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public abstract class HAPDefinitionEntityComplexWithDataExpressionGroup extends HAPDefinitionEntityInDomainComplex{

	static final public String ATTR_DATAEEXPRESSIONGROUP = "dataExpressionGroup";  
	
	public HAPDefinitionEntityComplexWithDataExpressionGroup() {}
	
	protected HAPDefinitionEntityComplexWithDataExpressionGroup(String entityType) {
		super(entityType);
	}
	
	public HAPIdEntityInDomain getDataExpressionGroup() {    return this.getAttributeValueEntityId(ATTR_DATAEEXPRESSIONGROUP);     }
}
