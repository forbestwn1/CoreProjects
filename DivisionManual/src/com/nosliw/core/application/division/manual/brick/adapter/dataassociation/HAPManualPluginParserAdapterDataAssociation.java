package com.nosliw.core.application.division.manual.brick.adapter.dataassociation;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociation;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionParserDataAssociation;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPManualPluginParserAdapterDataAssociation  extends HAPManualDefinitionPluginParserBrickImpSimple{

	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualPluginParserAdapterDataAssociation(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerDataRule dataRuleMan) {
		super(HAPEnumBrickType.DATAASSOCIATION_100, HAPManualDefinitionAdapterDataAssociation.class, manualDivisionEntityMan, brickMan);
		this.m_dataRuleMan = dataRuleMan;
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionAdapterDataAssociation entity = (HAPManualDefinitionAdapterDataAssociation)brickManual;
		
		Object daObj =  ((JSONObject)jsonValue).opt(HAPManualDefinitionAdapterDataAssociation.DEFINITION);
		if(daObj instanceof JSONObject) {
			HAPDefinitionDataAssociation da = HAPDefinitionParserDataAssociation.buildDefinitionByJson((JSONObject)daObj, this.m_dataRuleMan);
			entity.addDataAssciation(da);
		}
		else if(daObj instanceof JSONArray) {
			JSONArray daArray = (JSONArray)daObj;
			for(int i=0; i<daArray.length(); i++) {
				HAPDefinitionDataAssociation da = HAPDefinitionParserDataAssociation.buildDefinitionByJson(daArray.getJSONObject(i), this.m_dataRuleMan);
				entity.addDataAssciation(da);
			}
		}
	}
}
