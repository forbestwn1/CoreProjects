package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructureDefinition;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualUtilityValueContext;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinition;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinitionAttribute;

public class HAPManualUtilityUITag {

	public static HAPManualDefinitionBrickValueContext createValueContextBrickFromUITagDefinition(HAPUITagDefinition uiTagDef, HAPManualManagerBrick manualDivisionEntityMan) {
		HAPManualDefinitionBrickValueContext valueContextBrick = (HAPManualDefinitionBrickValueContext)manualDivisionEntityMan.newBrickDefinition(HAPManualEnumBrickType.VALUECONTEXT_100);
		for(HAPWrapperValueStructureDefinition uiTagDefValueStructure : uiTagDef.getValueContext().getValueStructures()) {
			HAPManualUtilityValueContext.addValueStuctureWrapperToValueContextBrick(uiTagDefValueStructure, valueContextBrick, manualDivisionEntityMan);
		}
		return valueContextBrick;
	}
	
	public static Map<String, HAPDefinitionConstant> buildConstantDefinitions(HAPUITagDefinition uiTagDef, Map<String, String> attrValues){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();

		Map<String, HAPUITagDefinitionAttribute> tagAttrDefs = uiTagDef.getAttributeDefinition();
		for(String attrName : tagAttrDefs.keySet()) {
			String constantName = buildAttributeConstantName(attrName);
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(constantName, tagAttrDefs.get(attrName).getDefaultValue());
			out.put(constantName, constantDef);
		}
		
		for(String attrName : attrValues.keySet()) {
			String constantName = buildAttributeConstantName(attrName);
			HAPDefinitionConstant constantDef = new HAPDefinitionConstant(constantName, attrValues.get(attrName));
			out.put(constantName, constantDef);
		}
		return out;
	}
	
	private static String buildAttributeConstantName(String attrName) {
		return HAPConstantShared.NOSLIW_RESERVE_ATTRIBUTE + attrName;
	}
	
}
