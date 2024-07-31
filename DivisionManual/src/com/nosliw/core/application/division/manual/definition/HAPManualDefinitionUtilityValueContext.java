package com.nosliw.core.application.division.manual.definition;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualDefinitionContainerScriptExpression;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualUtilityScriptExpressionConstant;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualUtilityScriptExpressionParser;
import com.nosliw.core.application.valuestructure.HAPRootInValueStructure;

public class HAPManualDefinitionUtilityValueContext {

	public static void discoverConstantScript(HAPManualDefinitionBrickValueContext valueContext, HAPManualDefinitionContainerScriptExpression scriptExpressionContainer) {
		if(valueContext==null) {
			return;
		}
		for(HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper : valueContext.getManualValueStructures()) {
			HAPValueStructureDefinition valueStructure = valueStructureWrapper.getValueStructureBlock().getValue();
			for(HAPRootInValueStructure root : valueStructure.getAllRoots()) {
				//root name
				String name = root.getName();
				if(HAPManualUtilityScriptExpressionParser.isScriptExpression(name)) {
					String scriptExpressionId = scriptExpressionContainer.addScriptExpression(name);
					root.setName(HAPManualUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
				}

				HAPUtilityStructure.traverseElement(root.getDefinition(), null, new HAPProcessorStructureElement() {
					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						if(eleInfo.getElement() instanceof HAPElementStructureLeafRelative) {
							HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
							String relativePath = relativeEle.getReference().getElementPath();
							if(HAPManualUtilityScriptExpressionParser.isScriptExpression(relativePath)) {
								String relativePathId = scriptExpressionContainer.addScriptExpression(relativePath);
								relativeEle.getReference().setElementPath(HAPManualUtilityScriptExpressionConstant.makeIdLiterate(relativePathId));
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

	public static void solidateConstantScript(HAPManualDefinitionBrickValueContext valueContext, Map<String, Object> values) {
		if(valueContext==null) {
			return;
		}
		for(HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper : valueContext.getManualValueStructures()) {
			HAPValueStructureDefinition valueStructure = valueStructureWrapper.getValueStructureBlock().getValue();
			for(HAPRootInValueStructure root : valueStructure.getAllRoots()) {
				//root name
				String name = root.getName();
				String nameId = HAPManualUtilityScriptExpressionConstant.isIdLterate(name);
				if(nameId!=null) {
					root.setName(values.get(nameId)+"");
				}

				HAPUtilityStructure.traverseElement(root.getDefinition(), null, new HAPProcessorStructureElement() {
					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						if(eleInfo.getElement() instanceof HAPElementStructureLeafRelative) {
							HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
							String relativePath = relativeEle.getReference().getElementPath();
							String relativePathId = HAPManualUtilityScriptExpressionConstant.isIdLterate(relativePath);
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

}
