package com.nosliw.data.core.expression;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

//entity that can is runnable within runtime environment
public abstract class HAPExecutableExpressionImp extends HAPExecutableImp implements HAPExecutableExpression{

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		
		Map<String, HAPExecutableExpressionItem> expressionItems = this.getExpressions();
		for(String name : expressionItems.keySet()) {
			dependency.addAll(expressionItems.get(name).getResourceDependency(runtimeInfo, resourceManager));
		}
	}
}
