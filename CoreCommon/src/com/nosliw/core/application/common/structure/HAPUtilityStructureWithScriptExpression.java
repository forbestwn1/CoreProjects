package com.nosliw.core.application.common.structure;

import java.util.Map;

import com.nosliw.core.application.common.scriptexpressio.HAPUtilityScriptExpressionConstant;
import com.nosliw.core.application.common.scriptexpressio.HAPUtilityScriptExpressionParser;
import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionContainerScriptExpression;

public class HAPUtilityStructureWithScriptExpression {

	public static void discoverConstantScript(HAPValueStructure valueStructure, HAPDefinitionContainerScriptExpression scriptExpressionContainer) {
		for(HAPRootInStructure root : valueStructure.getRoots().values()) {
			//root name
			String name = root.getName();
			if(HAPUtilityScriptExpressionParser.isScriptExpression(name)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(name);
				root.setName(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			//root id 
			String id = root.getId();
			if(HAPUtilityScriptExpressionParser.isScriptExpression(id)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(id);
				root.setId(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			//root status
			String rootStatus = root.getStatus();
			if(HAPUtilityScriptExpressionParser.isScriptExpression(rootStatus)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(rootStatus);
				root.setStatus(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			if(root.getDefinition() instanceof HAPElementStructureLeafRelative) {
				HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)root.getDefinition();
				String relativePath = relativeEle.getReference().getElementPath();
				if(HAPUtilityScriptExpressionParser.isScriptExpression(relativePath)) {
					String relativePathId = scriptExpressionContainer.addScriptExpression(relativePath);
					relativeEle.getReference().setElementPath(HAPUtilityScriptExpressionConstant.makeIdLiterate(relativePathId));
				}
			}
		}
	}
	
	public static void solidateConstantScript(HAPValueStructure valueStructure, Map<String, Object> values) {
		for(HAPRootInStructure root : valueStructure.getRoots().values()) {
			//root name
			String name = root.getName();
			String nameId = HAPUtilityScriptExpressionConstant.isIdLterate(name);
			if(nameId!=null) {
				root.setName(values.get(nameId)+"");
			}

			String id = root.getId();
			String idId = HAPUtilityScriptExpressionConstant.isIdLterate(name);
			if(idId!=null) {
				root.setId(values.get(idId)+"");
			}

			//root status
			{
				String rootStatus = root.getStatus();
				String statusId = HAPUtilityScriptExpressionConstant.isIdLterate(rootStatus);
				if(statusId!=null) {
					root.setStatus(values.get(statusId)+"");
				}
			}

			if(root.getDefinition() instanceof HAPElementStructureLeafRelative) {
				HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)root.getDefinition();
				String relativePath = relativeEle.getReference().getElementPath();
				String relativePathId = HAPUtilityScriptExpressionConstant.isIdLterate(relativePath);
				if(relativePathId!=null) {
					relativeEle.getReference().setElementPath(values.get(relativePathId)+"");
				}
			}
		}
	}

	
}
