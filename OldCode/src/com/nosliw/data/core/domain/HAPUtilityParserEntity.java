package com.nosliw.data.core.domain;

import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPUtilityParserEntity {

	public static HAPIdEntityInDomain parseReferenceResource(HAPResourceId resourceId, HAPContextParser parserContext, HAPManagerResourceDefinition resourceDefinitionManager) {
		HAPIdEntityInDomain out = parserContext.getCurrentDomain().addEntityOrReference(resourceId);
		HAPInfoEntityInDomainDefinition entityInfo = parserContext.getCurrentDomain().getEntityInfoDefinition(out);
		if(!entityInfo.isGlobalComplexResourceReference()) {
			//load resource into global domain except for global complex entity resource
			 resourceDefinitionManager.getResourceDefinition(resourceId, parserContext.getGlobalDomain(), parserContext.getCurrentDomainId());  //kkkk
		}
		return out;
	}
	
}
