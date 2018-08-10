package com.nosliw.uiresource.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.constant.HAPConstantDef;
import com.nosliw.uiresource.page.HAPUIDefinitionUnit;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitTag;

public class HAPConstantProcessor {

	/**
	 * Calculate all the constant values in ConstantDef
	 * @param parentConstants
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 */
	public static void processConstantDefs(
			HAPUIDefinitionUnit uiDefinitionUnit,
			Map<String, HAPConstantDef> parentConstants,
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){
		//build constants by merging parent with current
		Map<String, HAPConstantDef> contextConstants = new LinkedHashMap<String, HAPConstantDef>();
		if(parentConstants!=null)   contextConstants.putAll(parentConstants);
		contextConstants.putAll(uiDefinitionUnit.getConstantDefs());
		
		//process all constants defined in this domain
		com.nosliw.data.core.script.constant.HAPConstantUtility.processConstantDefs(contextConstants, expressionMan, runtime);
		uiDefinitionUnit.setConstantDefs(contextConstants);

		for(String constantName : contextConstants.keySet()) {
			HAPConstantDef constantDef = contextConstants.get(constantName);
			uiDefinitionUnit.addConstant(constantName, constantDef.getValue());

			//add constant value to expression context if it is data value
			HAPData dataValue = constantDef.getDataValue();
			if(dataValue!=null)    uiDefinitionUnit.getExpressionContext().addConstant(constantName, dataValue);
		}

		//process constants in child
		for(HAPUIDefinitionUnitTag tag : uiDefinitionUnit.getUITags()){
			processConstantDefs(tag, contextConstants, expressionMan, runtime);
		}
	}

}
