package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.core.application.uitag.HAPUITagDefinition;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.ui.uicontent.HAPBlockComplexUICustomerTagDebugger;
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

		//tag definition
		debuggerBrickDef.setUITagDefinition(uiTagDef);

		//attribute
		JSONObject attrJson = jsonObj.optJSONObject(HAPBlockComplexUICustomerTagDebugger.ATTRIBUTE);
		for(Object key : attrJson.keySet()) {
			String name = (String)key;
			debuggerBrickDef.setTagAttribute(name, attrJson.getString(name));
		}

		//build value context from tag definition
		HAPManualDefinitionBrickValueContext valueContextBrick = HAPUtilityUITag.createValueContextBrickFromUITagDefinition(uiTagDef, this.getManualDivisionEntityManager());
		debuggerBrickDef.setValueContextBrick(valueContextBrick);

		//child
		JSONArray childJsonArray = jsonObj.optJSONArray(HAPBlockComplexUICustomerTagDebugger.CONTENT);
		if(childJsonArray!=null){
			HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger wrapperBlockDef = (HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger)this.getManualDivisionEntityManager().newBrickDefinition(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100);
			for(int i=0; i<childJsonArray.length(); i++) {
				JSONObject elementObj = childJsonArray.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
					wrapperBlockDef.addChild((HAPManualDefinitionBlockComplexUICustomerTagDebugger)HAPManualDefinitionUtilityParserBrickFormatJson.parseBrick(elementObj, HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, parseContext, getManualDivisionEntityManager(), getBrickManager()));
				}
			}
			debuggerBrickDef.setContentWrapper(wrapperBlockDef);
		}
		
	}

}
