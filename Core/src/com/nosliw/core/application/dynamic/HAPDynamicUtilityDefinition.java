package com.nosliw.core.application.dynamic;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.brickcriteria.HAPCriteriaBrick;
import com.nosliw.core.application.entity.brickcriteria.HAPRestrainBrick;
import com.nosliw.core.application.entity.brickcriteria.HAPUtilityBrickCriteria;

public class HAPDynamicUtilityDefinition {

	public static <T extends HAPRestrainBrick> List<T> getCriteriaRestrain(HAPDynamicDefinitionItemLeaf dynamicDefinitionItemLeaf, String restrainType, Class<T> restrainClass) {
		List<HAPCriteriaBrick> brickCriterias = new ArrayList<HAPCriteriaBrick>(); 
		getBrickCriterias(dynamicDefinitionItemLeaf.getCriteria(), brickCriterias);
		
		List<T> out = new ArrayList<T>();
		brickCriterias.forEach(criteria->{
			out.addAll((List<T>)HAPUtilityBrickCriteria.getCriteriaRestrain(criteria, restrainType));
		});
        return out;
	}

	public static List<HAPRestrainBrick> getCriteriaRestrain(HAPDynamicDefinitionItemLeaf dynamicDefinitionItemLeaf, String restrainType) {
		List<HAPCriteriaBrick> brickCriterias = new ArrayList<HAPCriteriaBrick>(); 
		getBrickCriterias(dynamicDefinitionItemLeaf.getCriteria(), brickCriterias);
		
		List<HAPRestrainBrick> out = new ArrayList<HAPRestrainBrick>();
		brickCriterias.forEach(criteria->{
			out.addAll(HAPUtilityBrickCriteria.getCriteriaRestrain(criteria, restrainType));
		});
        return out;
	}

	private static void getBrickCriterias(HAPDynamicDefinitionCriteria dynamicCriteriaDef, List<HAPCriteriaBrick> out) {
		String type = dynamicCriteriaDef.getStructure();
		if(type.equals(HAPConstantShared.DYNAMICDEFINITION_CRITERIA_SIMPLE)) {
			out.add(((HAPDynamicDefinitionCriteriaSimple)dynamicCriteriaDef).getCriteriaDefinition());
		}
		else if(type.equals(HAPConstantShared.DYNAMICDEFINITION_CRITERIA_COMPLEX)) {
			((HAPDynamicDefinitionCriteriaComplex)dynamicCriteriaDef).getChildren().forEach(child->getBrickCriterias(child, out));
		}
	}
	
}
