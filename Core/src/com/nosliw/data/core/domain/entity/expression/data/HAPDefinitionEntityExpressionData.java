package com.nosliw.data.core.domain.entity.expression.data;

import java.util.List;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.container.HAPDefinitionEntityContainerComplex;
import com.nosliw.data.core.entity.division.manual.HAPManualEntityComplex;

public abstract class HAPDefinitionEntityExpressionData extends HAPManualEntityComplex{

	public static final String ATTR_REFERENCES = "references";

	public HAPDefinitionEntityExpressionData() {
	}

	public abstract List<HAPDefinitionExpressionData> getAllExpressionItems();

	public HAPIdEntityInDomain getReferencesContainerId() {    return (HAPIdEntityInDomain)this.getAttributeValue(ATTR_REFERENCES);     }
	
	public HAPDefinitionEntityContainerComplex getReferencesContainer(HAPDomainEntityDefinitionGlobal globalDomain){
		return (HAPDefinitionEntityContainerComplex)globalDomain.getEntityInfoDefinition(getReferencesContainerId()).getEntity();    
	}
	
}
