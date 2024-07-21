package com.nosliw.core.application.common.structure;

import java.util.List;

import com.nosliw.data.core.scriptexpression.HAPWithConstantScriptExpression;

public interface HAPValueContextDefinition extends HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	List<HAPWrapperValueStructure> getValueStructures();
	
}
