package com.nosliw.core.application.common.structure;

import java.util.List;

import com.nosliw.core.application.division.manual.common.scriptexpression.HAPWithConstantScriptExpression;

public interface HAPValueContextDefinition extends HAPWithConstantScriptExpression{

	public static final String VALUESTRUCTURE = "valueStructure";
	
	List<HAPWrapperValueStructure> getValueStructures();
	
}
