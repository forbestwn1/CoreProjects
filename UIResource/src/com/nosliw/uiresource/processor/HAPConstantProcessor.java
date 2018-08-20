package com.nosliw.uiresource.processor;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.constant.HAPConstantDef;
import com.nosliw.data.core.script.constant.HAPConstantUtility;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnitTag;

public class HAPConstantProcessor {

	/**
	 * Calculate all the constant values in ConstantDef
	 * @param parentConstants
	 * @param idGenerator
	 * @param expressionMan
	 * @param runtime
	 */
	public static HAPContextGroup processConstantContext(
			HAPContextGroup contextGroup,
			HAPContextGroup parentContextGroup,
			HAPExpressionSuiteManager expressionMan, 
			HAPRuntime runtime){

		HAPContextGroup out = contextGroup.clone();
		//merge constants with parent
		for(String contextCategary : HAPContextGroup.getInheritableContextTypes()) {
			for(String name : parentContextGroup.getContext(contextCategary).getElementNames()) {
				if(parentContextGroup.getElement(contextCategary, name).getType().equals(HAPConstant.UIRESOURCE_ROOTTYPE_CONSTANT)) {
					if(contextGroup.getElement(contextCategary, name)==null) {
						out.addElement(name, contextGroup.getElement(contextCategary, name).cloneContextNodeRoot(), contextCategary);
					}
				}
			}
		}
		
		//solid constant
	
		
		return out;
	}

}
