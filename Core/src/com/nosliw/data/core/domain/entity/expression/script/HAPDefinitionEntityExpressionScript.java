package com.nosliw.data.core.domain.entity.expression.script;

import java.util.List;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.container.HAPDefinitionEntityContainerComplex;

public abstract class HAPDefinitionEntityExpressionScript extends HAPDefinitionEntityInDomainComplex{

	public static final String ATTR_REFERENCES = "references";

	public abstract List<HAPDefinitionExpression> getAllExpressionItems();

	public HAPIdEntityInDomain getReferencesContainerId() {    return (HAPIdEntityInDomain)this.getAttributeValue(ATTR_REFERENCES);     }
	
	public HAPDefinitionEntityContainerComplex getReferencesContainer(HAPDomainEntityDefinitionGlobal globalDomain){
		return (HAPDefinitionEntityContainerComplex)globalDomain.getEntityInfoDefinition(getReferencesContainerId()).getEntity();    
	}
}
