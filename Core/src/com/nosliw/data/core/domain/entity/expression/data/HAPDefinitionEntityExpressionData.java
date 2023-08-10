package com.nosliw.data.core.domain.entity.expression.data;

import java.util.List;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.container.HAPDefinitionEntityComplexContainer;

public abstract class HAPDefinitionEntityExpressionData extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_REFERENCES = "references";

	public HAPDefinitionEntityExpressionData() {
	}

	public abstract List<HAPDefinitionExpressionData> getAllExpressionItems();

	public HAPIdEntityInDomain getReferencesContainerId() {    return (HAPIdEntityInDomain)this.getAttributeValue(ATTR_REFERENCES);     }
	
	public HAPDefinitionEntityComplexContainer getReferencesContainer(HAPDomainEntityDefinitionGlobal globalDomain){
		return (HAPDefinitionEntityComplexContainer)globalDomain.getEntityInfoDefinition(getReferencesContainerId()).getEntity();    
	}
	
}
