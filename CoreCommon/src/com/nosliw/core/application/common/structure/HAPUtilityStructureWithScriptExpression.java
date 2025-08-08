package com.nosliw.core.application.common.structure;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.common.scriptexpression.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPManualUtilityScriptExpressionParser;
import com.nosliw.core.application.common.scriptexpression.HAPUtilityScriptExpressionConstant;
import com.nosliw.core.application.common.structure22.HAPElementStructure;
import com.nosliw.core.application.common.structure22.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure22.HAPInfoElement;
import com.nosliw.core.application.common.structure22.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure22.HAPRootInStructure;
import com.nosliw.core.application.common.structure22.HAPValueStructure;

public class HAPUtilityStructureWithScriptExpression {

	public static void discoverConstantScript(HAPValueStructure valueStructure, HAPDefinitionContainerScriptExpression scriptExpressionContainer) {
		for(HAPRootInStructure root : valueStructure.getRoots().values()) {
			//root name
			String name = root.getName();
			if(HAPManualUtilityScriptExpressionParser.isScriptExpression(name)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(name);
				root.setName(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			//root id 
			String id = root.getId();
			if(HAPManualUtilityScriptExpressionParser.isScriptExpression(id)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(id);
				root.setId(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			//root status
			String rootStatus = root.getStatus();
			if(HAPManualUtilityScriptExpressionParser.isScriptExpression(rootStatus)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(rootStatus);
				root.setStatus(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			

			HAPUtilityStructure.traverseElement(root.getDefinition(), null, new HAPProcessorStructureElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getElement() instanceof HAPElementStructureLeafRelative) {
						HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
						String relativePath = relativeEle.getReference().getElementPath();
						if(HAPManualUtilityScriptExpressionParser.isScriptExpression(relativePath)) {
							String relativePathId = scriptExpressionContainer.addScriptExpression(relativePath);
							relativeEle.getReference().setElementPath(HAPUtilityScriptExpressionConstant.makeIdLiterate(relativePathId));
						}
						return Pair.of(false, null);
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);
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

			HAPUtilityStructure.traverseElement(root.getDefinition(), null, new HAPProcessorStructureElement() {
				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
					if(eleInfo.getElement() instanceof HAPElementStructureLeafRelative) {
						HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
						String relativePath = relativeEle.getReference().getElementPath();
						String relativePathId = HAPUtilityScriptExpressionConstant.isIdLterate(relativePath);
						if(relativePathId!=null) {
							relativeEle.getReference().setElementPath(values.get(relativePathId)+"");
						}
						return Pair.of(false, null);
					}
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);
		}
	}

	
}
