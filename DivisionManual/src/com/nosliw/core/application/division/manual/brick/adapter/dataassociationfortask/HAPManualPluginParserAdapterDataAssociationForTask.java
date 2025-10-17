package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionDataAssociationForTask;
import com.nosliw.core.application.common.dataassociation.definition.HAPDefinitionParserDataAssociationForTask;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPManualPluginParserAdapterDataAssociationForTask  extends HAPManualDefinitionPluginParserBrickImpSimple{

	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualPluginParserAdapterDataAssociationForTask(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerDataRule dataRuleMan) {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, HAPManualDefinitionAdapterDataAssociationForTask.class, manualDivisionEntityMan, brickMan);
		this.m_dataRuleMan = dataRuleMan;
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionAdapterDataAssociationForTask entity = (HAPManualDefinitionAdapterDataAssociationForTask)brickManual;
		
		JSONObject daJsonObj =  ((JSONObject)jsonValue).optJSONObject(HAPManualDefinitionAdapterDataAssociationForTask.DEFINITION);
		HAPDefinitionDataAssociationForTask dataAssociationForTask = HAPDefinitionParserDataAssociationForTask.parse(daJsonObj, m_dataRuleMan);
		entity.setDataAssciation(dataAssociationForTask);
	}
}
