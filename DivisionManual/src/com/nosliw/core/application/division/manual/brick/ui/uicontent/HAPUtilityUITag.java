package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.uitag.HAPUITagAttributeDefinition;
import com.nosliw.core.application.uitag.HAPUITagDefinition;

public class HAPUtilityUITag {

	public static HAPManualDefinitionBrickValueContext createValueContextBrickFromUITagDefinition(HAPUITagDefinition uiTagDef, HAPManualManagerBrick manualDivisionEntityMan) {
		HAPManualDefinitionBrickValueContext valueContextBrick = (HAPManualDefinitionBrickValueContext)manualDivisionEntityMan.newBrickDefinition(HAPManualEnumBrickType.VALUECONTEXT_100);
		for(HAPWrapperValueStructure uiTagDefValueStructure : uiTagDef.getValueContext().getValueStructures()) {
			HAPManualDefinitionBrickWrapperValueStructure manualWrapperBrickValueStrucutre = (HAPManualDefinitionBrickWrapperValueStructure)manualDivisionEntityMan.newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);

			HAPManualDefinitionBrickValueStructure manualBrickValueStrucutre = (HAPManualDefinitionBrickValueStructure)manualDivisionEntityMan.newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTURE_100);
			manualBrickValueStrucutre.setValue(uiTagDefValueStructure.getValueStructure());
			manualWrapperBrickValueStrucutre.setValueStructure(manualBrickValueStrucutre);
			manualWrapperBrickValueStrucutre.setGroupType(uiTagDefValueStructure.getGroupType());
			manualWrapperBrickValueStrucutre.setInheritMode(uiTagDefValueStructure.getInheritMode());
			uiTagDefValueStructure.cloneToEntityInfo(manualWrapperBrickValueStrucutre);
		
			valueContextBrick.addValueStructure(manualWrapperBrickValueStrucutre);
		}
		return valueContextBrick;
	}
	
	public static Map<String, HAPDefinitionConstant> getConstantDefinitions(HAPUITagDefinition uiTagDef, Map<String, String> attrValues){
		Map<String, HAPDefinitionConstant> out = new LinkedHashMap<String, HAPDefinitionConstant>();

		Map<String, HAPUITagAttributeDefinition> tagAttrDefs = uiTagDef.getAttributeDefinition();
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
