package com.nosliw.core.application.common.withvariable;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

public interface HAPPluginProcessorEntityWithVariable {

	//resolve variable
	void resolveVariable(HAPWithVariable withVariableEntity, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure);
	
	//discovery
	Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>>  discoverVariableCriteria(HAPWithVariable withVariableEntity, Map<String, HAPDataTypeCriteria> expectOutputs, HAPContainerVariableInfo varInfoContainer);

}
