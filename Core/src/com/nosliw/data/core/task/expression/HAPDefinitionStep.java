package com.nosliw.data.core.task.expression;

import java.util.Set;

import com.nosliw.data.core.task.HAPDefinitionComponent;

public abstract class HAPDefinitionStep extends HAPDefinitionComponent {

	abstract public String getType();
	
	abstract public Set<String> getVariableNames();
	
	abstract public Set<String> getReferenceNames();
	
}
