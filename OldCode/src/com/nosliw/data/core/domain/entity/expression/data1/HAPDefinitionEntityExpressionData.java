package com.nosliw.data.core.domain.entity.expression.data1;

import java.util.List;

import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.container.HAPDefinitionEntityContainerComplex;

public abstract class HAPDefinitionEntityExpressionData extends HAPManualBrickComplex{

	public static final String ATTR_REFERENCES = "references";

	public HAPDefinitionEntityExpressionData() {
	}

	public abstract List<HAPDefinitionExpressionData> getAllExpressionItems();

	public HAPIdEntityInDomain getReferencesContainerId() {    return (HAPIdEntityInDomain)this.getAttributeValueOfValue(ATTR_REFERENCES);     }
	
	public HAPDefinitionEntityContainerComplex getReferencesContainer(HAPDomainEntityDefinitionGlobal globalDomain){
		return (HAPDefinitionEntityContainerComplex)globalDomain.getEntityInfoDefinition(getReferencesContainerId()).getEntity();    
	}
	
}
