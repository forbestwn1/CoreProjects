package com.nosliw.core.application.division.manual.brick.ui.uicontent;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.ui.uicontent.HAPBlockComplexUICustomerTagDebugger;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpComplex;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.core.application.entity.uitag.HAPManagerUITag;
import com.nosliw.core.application.entity.uitag.HAPUITagDefinition;

public class HAPManualPluginParserBlockComplexUICustomerTagDebugger extends HAPManualDefinitionPluginParserBrickImpComplex{

	private HAPManagerUITag m_uiTagMan;
	
	public HAPManualPluginParserBlockComplexUICustomerTagDebugger(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerUITag uiTagMan) {
		super(HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, HAPManualDefinitionBlockComplexUICustomerTagDebugger.class, manualDivisionEntityMan, brickMan);
		this.m_uiTagMan = uiTagMan;
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockComplexUICustomerTagDebugger debuggerBrickDef = (HAPManualDefinitionBlockComplexUICustomerTagDebugger)brickDefinition;
		
		//entity info
		debuggerBrickDef.buildEntityInfoByJson(jsonObj);
		
		//tag id
		debuggerBrickDef.setUITagId(jsonObj.getString(HAPBlockComplexUICustomerTagDebugger.UITAGID));

		HAPUITagDefinition uiTagDef = this.m_uiTagMan.getUITagDefinition(debuggerBrickDef.getUITagId(), null);

		//tag definition
		debuggerBrickDef.setUITagDefinition(uiTagDef);

		//attribute
		JSONObject attrJson = jsonObj.optJSONObject(HAPBlockComplexUICustomerTagDebugger.ATTRIBUTE);
		for(Object key : attrJson.keySet()) {
			String name = (String)key;
			debuggerBrickDef.setTagAttribute(name, attrJson.getString(name));
		}

		//build value context from tag definition
		HAPManualDefinitionBrickValueContext valueContextBrick = HAPManualUtilityUITag.createValueContextBrickFromUITagDefinition(uiTagDef, this.getManualDivisionBrickManager());
		debuggerBrickDef.setValueContextBrick(valueContextBrick);

		//child
		JSONArray childJsonArray = jsonObj.optJSONArray(HAPBlockComplexUICustomerTagDebugger.CONTENT);
		if(childJsonArray!=null){
			HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger wrapperBlockDef = (HAPManualDefinitionBlockComplexUIWrapperContentInCustomerTagDebugger)this.getManualDivisionBrickManager().newBrickDefinition(HAPEnumBrickType.UIWRAPPERCONTENTCUSTOMERTAGDEBUGGER_100);
			for(int i=0; i<childJsonArray.length(); i++) {
				JSONObject elementObj = childJsonArray.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(elementObj)) {
					wrapperBlockDef.addChild((HAPManualDefinitionBlockComplexUICustomerTagDebugger)HAPManualDefinitionUtilityParserBrickFormatJson.parseBrick(elementObj, HAPEnumBrickType.UICUSTOMERTAGDEBUGGER_100, parseContext));
				}
			}
			debuggerBrickDef.setContentWrapper(wrapperBlockDef);
		}
		
	}

}
