package com.nosliw.core.application.common.withvariable;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPVariableInfo;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPUtilityWithVarible {

	public static void resolveVariable(HAPWithVariable withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure, HAPManagerWithVariablePlugin withVariableMan) {
		withVariableMan.getWithVariableEntityProcessPlugin(withVariableEntity.getWithVariableEntityType()).resolveVariable(withVariableEntity, varInfoContainer, resolveConfigure);
	}
	
	public static Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>>  discoverVariableCriteria(HAPWithVariable withVariableEntity, Map<String, HAPDataTypeCriteria> expections, HAPContainerVariableInfo varInfoContainer, HAPManagerWithVariablePlugin withVariableMan){
		return withVariableMan.getWithVariableEntityProcessPlugin(withVariableEntity.getWithVariableEntityType()).discoverVariableCriteria(withVariableEntity, expections, varInfoContainer);
	}

	public static Set<String> getVariableKeys(HAPWithVariable withVariableEntity, HAPManagerWithVariablePlugin withVariableMan){
		return withVariableMan.getWithVariableEntityProcessPlugin(withVariableEntity.getWithVariableEntityType()).getVariableKeys(withVariableEntity);
	}

	public static void buildVariableInfoInEntity(HAPWithVariable withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPManagerWithVariablePlugin withVariableMan) {
		Set<String> varKeys = getVariableKeys(withVariableEntity, withVariableMan);
		for(String key : varKeys) {
			withVariableEntity.addVariableInfo(new HAPVariableInfo(key, varInfoContainer.getVariableId(key)));
		}
	}
	
}
