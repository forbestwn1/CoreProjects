package com.nosliw.core.application.common.withvariable;

import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.valueport.HAPConfigureResolveElementReference;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

public interface HAPPluginProcessorEntityWithVariable {

	String getEntityType();
	
	//resolve variable
	void resolveVariable(Object withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure);
	
	//discovery
	Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>>  discoverVariableCriteria(Object withVariableEntity, Map<String, HAPDataTypeCriteria> expections, HAPContainerVariableInfo varInfoContainer);

	//all variables keys in this entity
	Set<String> getVariableKeys(Object withVariableEntity);
	
}
