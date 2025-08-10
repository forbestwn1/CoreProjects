package com.nosliw.core.application.common.withvariable;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPUtilityWithVarible {

	public static void resolveVariable(HAPWithVariable withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure, HAPManagerWithVariablePlugin withVariableMan, HAPRuntimeInfo runtimeInfo) {
		HAPPluginProcessorEntityWithVariable plugin = withVariableMan.getWithVariableEntityProcessPlugin(withVariableEntity.getWithVariableEntityType());
		plugin.resolveVariable(withVariableEntity, varInfoContainer, resolveConfigure, runtimeInfo);
	}
	
	public static Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>>  discoverVariableCriteria(HAPWithVariable withVariableEntity, Map<String, HAPDataTypeCriteria> expections, HAPContainerVariableInfo varInfoContainer, HAPManagerWithVariablePlugin withVariableMan){
		HAPPluginProcessorEntityWithVariable plugin = withVariableMan.getWithVariableEntityProcessPlugin(withVariableEntity.getWithVariableEntityType());
		return plugin.discoverVariableCriteria(withVariableEntity, expections, varInfoContainer);
	}

	public static Set<String> getVariableKeys(HAPWithVariable withVariableEntity, HAPManagerWithVariablePlugin withVariableMan){
		HAPPluginProcessorEntityWithVariable plugin = withVariableMan.getWithVariableEntityProcessPlugin(withVariableEntity.getWithVariableEntityType());
		return plugin.getVariableKeys(withVariableEntity);
	}

	public static void buildVariableInfoInEntity(HAPWithVariable withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPManagerWithVariablePlugin withVariableMan) {
		Set<String> varKeys = getVariableKeys(withVariableEntity, withVariableMan);
		for(String key : varKeys) {
			withVariableEntity.addVariableInfo(new HAPVariableInfo(key, varInfoContainer.getVariableId(key)));
		}
	}
	
}
