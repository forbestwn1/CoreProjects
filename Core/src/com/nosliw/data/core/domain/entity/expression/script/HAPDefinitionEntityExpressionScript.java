package com.nosliw.data.core.domain.entity.expression.script;

import java.util.List;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.container.HAPDefinitionEntityComplexContainer;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityComplexWithDataExpressionGroup;

public abstract class HAPDefinitionEntityExpressionScript extends HAPDefinitionEntityComplexWithDataExpressionGroup{

	public static final String ATTR_REFERENCES = "references";

	public abstract List<HAPDefinitionExpression> getAllExpressionItems();

	public HAPIdEntityInDomain getReferencesContainerId() {    return (HAPIdEntityInDomain)this.getAttributeValue(ATTR_REFERENCES);     }
	
	public HAPDefinitionEntityComplexContainer getReferencesContainer(HAPDomainEntityDefinitionGlobal globalDomain){
		return (HAPDefinitionEntityComplexContainer)globalDomain.getEntityInfoDefinition(getReferencesContainerId()).getEntity();    
	}
}