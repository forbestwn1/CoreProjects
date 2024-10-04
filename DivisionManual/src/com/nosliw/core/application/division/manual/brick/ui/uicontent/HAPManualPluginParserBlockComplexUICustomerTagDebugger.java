package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTagDebugger;
import com.nosliw.core.application.common.structure.HAPWrapperValueStructure;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.core.application.uitag.HAPUITagDefinition;
import com.nosliw.core.application.uitag.HAPUITagValueContextDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockComplexUICustomerTagDebugger extends HAPManualDefinitionPluginParserBrickImpComplex{

	public HAPManualPluginParserBlockComplexUICustomerTagDebugger(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, HAPManualDefinitionBlockComplexUICustomerTagDebugger.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockComplexUICustomerTagDebugger debuggerBrickDef = (HAPManualDefinitionBlockComplexUICustomerTagDebugger)brickDefinition;
		
		//entity info
		debuggerBrickDef.buildEntityInfoByJson(jsonObj);
		
		//tag id
		debuggerBrickDef.setUITagId(jsonObj.getString(HAPBlockComplexUICustomerTagDebugger.UITAGID));

		HAPUITagDefinition uiTagDef = this.getRuntimeEnvironment().getUITagManager().getUITagDefinition(debuggerBrickDef.getUITagId(), null);
		HAPUITagValueContextDefinition uiTagDefValueContext = uiTagDef.getValueContext();

		//tag definition
		debuggerBrickDef.setUITagDefinition(uiTagDef);

		//attribute
		JSONObject attrJson = jsonObj.optJSONObject(HAPBlockComplexUICustomerTagDebugger.ATTRIBUTE);
		for(Object key : attrJson.keySet()) {
			String name = (String)key;
			debuggerBrickDef.setTagAttribute(name, attrJson.getString(name));
		}

		//build value context
		HAPManualDefinitionBrickValueContext valueContextBrick = (HAPManualDefinitionBrickValueContext)this.getManualDivisionEntityManager().newBrickDefinition(HAPManualEnumBrickType.VALUECONTEXT_100);
		for(HAPWrapperValueStructure uiTagDefValueStructure : uiTagDefValueContext.getValueStructures()) {
			HAPManualDefinitionBrickWrapperValueStructure manualWrapperBrickValueStrucutre = (HAPManualDefinitionBrickWrapperValueStructure)this.getManualDivisionEntityManager().newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100);

			HAPManualDefinitionBrickValueStructure manualBrickValueStrucutre = (HAPManualDefinitionBrickValueStructure)this.getManualDivisionEntityManager().newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTURE_100);
			manualBrickValueStrucutre.setValue(uiTagDefValueStructure.getValueStructure());
			manualWrapperBrickValueStrucutre.setValueStructure(manualBrickValueStrucutre);
			manualWrapperBrickValueStrucutre.setGroupType(uiTagDefValueStructure.getGroupType());
			uiTagDefValueStructure.cloneToEntityInfo(manualWrapperBrickValueStrucutre);
		
			valueContextBrick.addValueStructure(manualWrapperBrickValueStrucutre);
		}
		debuggerBrickDef.setValueContextBrick(valueContextBrick);

		//child
		JSONArray childJsonArray = jsonObj.optJSONArray(HAPBlockComplexUICustomerTagDebugger.CHILD);
		if(childJsonArray!=null){
			for(int i=0; i<childJsonArray.length(); i++) {
				JSONObject elementObj = childJsonArray.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
					debuggerBrickDef.addChild((HAPManualDefinitionBlockComplexUICustomerTagDebugger)HAPManualDefinitionUtilityParserBrickFormatJson.parseBrick(elementObj, HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, parseContext, getManualDivisionEntityManager(), getBrickManager()));
				}
			}
		}
		
	}

}
