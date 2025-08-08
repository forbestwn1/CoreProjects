package com.nosliw.core.application.division.manual.core.definition1;

import java.util.Map;

import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPManualUtilityScriptExpressionParser;
import com.nosliw.core.application.common.scriptexpression.HAPUtilityScriptExpressionConstant;
import com.nosliw.core.application.common.structure.HAPValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.xxx.application.common.structure.HAPUtilityStructureWithScriptExpression;

public class HAPManualDefinitionUtilityValueContext {

	public static void discoverConstantScript(HAPManualDefinitionBrickValueContext valueContext, HAPDefinitionContainerScriptExpression scriptExpressionContainer) {
		if(valueContext==null) {
			return;
		}
		for(HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper : valueContext.getManualValueStructures()) {
			//value structure status
			String vsStatus = valueStructureWrapper.getStatus();
			if(HAPManualUtilityScriptExpressionParser.isScriptExpression(vsStatus)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(vsStatus);
				valueStructureWrapper.setStatus(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			HAPValueStructure valueStructure = valueStructureWrapper.getValueStructureBlock().getValue();
			HAPUtilityStructureWithScriptExpression.discoverConstantScript(valueStructure, scriptExpressionContainer);
		}
	}

	public static void solidateConstantScript(HAPManualDefinitionBrickValueContext valueContext, Map<String, Object> values) {
		if(valueContext==null) {
			return;
		}
		for(HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper : valueContext.getManualValueStructures()) {
			{
				//value structure status
				String vsStatus = valueStructureWrapper.getStatus();
				String statusId = HAPUtilityScriptExpressionConstant.isIdLterate(vsStatus);
				if(statusId!=null) {
					valueStructureWrapper.setStatus(values.get(statusId)+"");
				}
			}

			HAPValueStructure valueStructure = valueStructureWrapper.getValueStructureBlock().getValue();
			HAPUtilityStructureWithScriptExpression.solidateConstantScript(valueStructure, values);
		}
	}

}
