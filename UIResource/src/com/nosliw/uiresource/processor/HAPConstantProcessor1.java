package com.nosliw.uiresource.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;

public class HAPConstantProcessor1 {
/*
	
//	 * Calculate all the constant values in ConstantDef
	public static void processConstantDefs(
			HAPDefinitionUIUnit uiDefinitionUnit,
			Map<String, HAPConstantDef> parentConstants,
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		//build constants by merging parent with current
		Map<String, HAPConstantDef> contextConstants = new LinkedHashMap<String, HAPConstantDef>();
		if(parentConstants!=null)   contextConstants.putAll(parentConstants);
		contextConstants.putAll(uiDefinitionUnit.getConstantDefs());
		
		//process all constants defined in this domain
		HAPConstantUtility.processConstantDefs(contextConstants, expressionMan, runtime);
		uiDefinitionUnit.setConstantDefs(contextConstants);

		for(String constantName : contextConstants.keySet()) {
			HAPConstantDef constantDef = contextConstants.get(constantName);
			uiDefinitionUnit.addConstant(constantName, constantDef.getValue());

			//add constant value to expression context if it is data value
			HAPData dataValue = constantDef.getDataValue();
			if(dataValue!=null)    uiDefinitionUnit.getExpressionContext().addConstant(constantName, dataValue);
		}

		//process constants in child
		for(HAPDefinitionUIUnitTag tag : uiDefinitionUnit.getUITags()){
			processConstantDefs(tag, contextConstants, expressionMan, runtime);
		}
	}
*/
}
