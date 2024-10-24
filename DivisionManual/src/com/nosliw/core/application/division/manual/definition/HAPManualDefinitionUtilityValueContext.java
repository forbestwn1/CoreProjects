package com.nosliw.core.application.division.manual.definition;

import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.HAPValueStructureDefinition;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualDefinitionContainerScriptExpression;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualUtilityScriptExpressionConstant;
import com.nosliw.core.application.division.manual.common.scriptexpression.HAPManualUtilityScriptExpressionParser;

public class HAPManualDefinitionUtilityValueContext {

	public static void discoverConstantScript(HAPManualDefinitionBrickValueContext valueContext, HAPManualDefinitionContainerScriptExpression scriptExpressionContainer) {
		if(valueContext==null) {
			return;
		}
		for(HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper : valueContext.getManualValueStructures()) {
			//value structure status
			String vsStatus = valueStructureWrapper.getStatus();
			if(HAPManualUtilityScriptExpressionParser.isScriptExpression(vsStatus)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(vsStatus);
				valueStructureWrapper.setStatus(HAPManualUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			HAPValueStructureDefinition valueStructure = valueStructureWrapper.getValueStructureBlock().getValue();
			for(HAPRootInStructure root : valueStructure.getRoots().values()) {
				//root name
				String name = root.getName();
				if(HAPManualUtilityScriptExpressionParser.isScriptExpression(name)) {
					String scriptExpressionId = scriptExpressionContainer.addScriptExpression(name);
					root.setName(HAPManualUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
				}
				
				//root id 
				String id = root.getId();
				if(HAPManualUtilityScriptExpressionParser.isScriptExpression(id)) {
					String scriptExpressionId = scriptExpressionContainer.addScriptExpression(id);
					root.setId(HAPManualUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
				}
				
				//root status
				String rootStatus = root.getStatus();
				if(HAPManualUtilityScriptExpressionParser.isScriptExpression(rootStatus)) {
					String scriptExpressionId = scriptExpressionContainer.addScriptExpression(rootStatus);
					root.setStatus(HAPManualUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
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
			{
				//value structure status
				String vsStatus = valueStructureWrapper.getStatus();
				String statusId = HAPManualUtilityScriptExpressionConstant.isIdLterate(vsStatus);
				if(statusId!=null) {
					valueStructureWrapper.setStatus(values.get(statusId)+"");
				}
			}

			HAPValueStructureDefinition valueStructure = valueStructureWrapper.getValueStructureBlock().getValue();
			for(HAPRootInStructure root : valueStructure.getRoots().values()) {
				//root name
				String name = root.getName();
				String nameId = HAPManualUtilityScriptExpressionConstant.isIdLterate(name);
				if(nameId!=null) {
					root.setName(values.get(nameId)+"");
				}

				String id = root.getId();
				String idId = HAPManualUtilityScriptExpressionConstant.isIdLterate(name);
				if(idId!=null) {
					root.setId(values.get(idId)+"");
				}

				//root status
				{
					String rootStatus = root.getStatus();
					String statusId = HAPManualUtilityScriptExpressionConstant.isIdLterate(rootStatus);
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
