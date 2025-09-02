package com.nosliw.core.application.common.dynamiccriteria;

public class HAPUtilityDynamicCriteria {

	public static HAPCriteriaDynamicSimpleoFacade getSimpleFacadeDynamicCriteria(HAPCriteriaDynamic dynamicCriteria, String facade) {
		HAPCriteriaDynamicSimpleoFacade out = null;
		if(dynamicCriteria instanceof HAPCriteriaDynamicSimpleoFacade){
			HAPCriteriaDynamicSimpleoFacade facadDynamicCriteria = (HAPCriteriaDynamicSimpleoFacade)dynamicCriteria;
			if(facadDynamicCriteria.getFacade().getName().equals(facade)){
				return facadDynamicCriteria;
			}
		}
		return out;
	}

	public static HAPRestrainBrickTypeFacade getSimpleFacadeDynamicCriteriaRestrain(HAPCriteriaDynamic dynamicCriteria, String facadeName, String restrainType) {
		HAPCriteriaDynamicSimpleoFacade facadeCriteria = getSimpleFacadeDynamicCriteria(dynamicCriteria, facadeName);
		if(facadeCriteria!=null) {
			for(HAPRestrainBrickTypeFacade restrain : facadeCriteria.getRestains()) {
				if(restrain.getType().equals(restrainType)) {
					return restrain;
				}
			}
		}
		return null;
	}
	
}
