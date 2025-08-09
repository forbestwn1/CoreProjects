package com.nosliw.core.application.common.withvariable;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.common.structure.reference.HAPConfigureResolveElementReference;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public interface HAPPluginProcessorEntityWithVariable<T extends HAPWithVariable> {

	String getEntityType();
	
	//resolve variable
	void resolveVariable(T withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure, HAPRuntimeInfo runtimeInfo);
	
	//discovery
	Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>>  discoverVariableCriteria(T withVariableEntity, Map<String, HAPDataTypeCriteria> expections, HAPContainerVariableInfo varInfoContainer);

	//all variables keys in this entity
	Set<String> getVariableKeys(T withVariableEntity);
	
}
