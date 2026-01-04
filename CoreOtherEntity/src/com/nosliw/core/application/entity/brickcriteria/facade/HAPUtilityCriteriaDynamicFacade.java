package com.nosliw.core.application.entity.brickcriteria.facade;

import com.nosliw.core.application.dynamic.HAPDynamicDefinitionCriteria;

public class HAPUtilityCriteriaDynamicFacade {

	public static HAPCriteriaBrickFacade getSimpleFacadeDynamicCriteria(HAPDynamicDefinitionCriteria dynamicCriteria, String facade) {
		HAPCriteriaBrickFacade out = null;
		if(dynamicCriteria instanceof HAPCriteriaBrickFacade){
			HAPCriteriaBrickFacade facadDynamicCriteria = (HAPCriteriaBrickFacade)dynamicCriteria;
			if(facadDynamicCriteria.getFacade().getName().equals(facade)){
				return facadDynamicCriteria;
			}
		}
		return out;
	}

	public static HAPRestrainBrickFacade getSimpleFacadeDynamicCriteriaRestrain(HAPDynamicDefinitionCriteria dynamicCriteria, String facadeName, String restrainType) {
		HAPCriteriaBrickFacade facadeCriteria = getSimpleFacadeDynamicCriteria(dynamicCriteria, facadeName);
		if(facadeCriteria!=null) {
			for(HAPRestrainBrickFacade restrain : facadeCriteria.getRestains()) {
				if(restrain.getType().equals(restrainType)) {
					return restrain;
				}
			}
		}
		return null;
	}
	
}
