package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualParserDataAssociation;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserAdapterDataAssociation  extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserAdapterDataAssociation(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.DATAASSOCIATION_100, HAPManualDefinitionAdapterDataAssociation.class, manualDivisionEntityMan, brickMan);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionAdapterDataAssociation entity = (HAPManualDefinitionAdapterDataAssociation)brickManual;
		
		Object daObj =  ((JSONObject)jsonValue).opt(HAPManualDefinitionAdapterDataAssociation.DEFINITION);
		if(daObj instanceof JSONObject) {
			HAPManualDataAssociation da = HAPManualParserDataAssociation.buildDefinitionByJson((JSONObject)daObj);
			entity.addDataAssciation(da);
		}
		else if(daObj instanceof JSONArray) {
			JSONArray daArray = (JSONArray)daObj;
			for(int i=0; i<daArray.length(); i++) {
				HAPManualDataAssociation da = HAPManualParserDataAssociation.buildDefinitionByJson(daArray.getJSONObject(i));
				entity.addDataAssciation(da);
			}
		}
	}
}
