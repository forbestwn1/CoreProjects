package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserAdapterDataAssociation  extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserAdapterDataAssociation(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATION_100, HAPManualAdapterDataAssciation.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick brickManual, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualAdapterDataAssciation entity = (HAPManualAdapterDataAssciation)brickManual;
		
		Object daObj =  ((JSONObject)jsonValue).opt(HAPManualAdapterDataAssciation.DATAASSOCIATION);
		if(daObj instanceof JSONObject) {
			HAPDefinitionDataAssociation da = HAPParserDataAssociation.buildDefinitionByJson((JSONObject)daObj);
			entity.addDataAssciation(da);
		}
		else if(daObj instanceof JSONArray) {
			JSONArray daArray = (JSONArray)daObj;
			for(int i=0; i<daArray.length(); i++) {
				HAPDefinitionDataAssociation da = HAPParserDataAssociation.buildDefinitionByJson(daArray.getJSONObject(i));
				entity.addDataAssciation(da);
			}
		}
	}
}