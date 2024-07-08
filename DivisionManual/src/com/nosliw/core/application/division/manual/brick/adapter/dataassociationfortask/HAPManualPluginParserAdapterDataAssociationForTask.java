package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserAdapterDataAssociationForTask  extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserAdapterDataAssociationForTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, HAPManualAdapterDataAssociationForTask.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualAdapterDataAssociationForTask entity = (HAPManualAdapterDataAssociationForTask)brickManual;
		
		JSONObject daJsonObj =  ((JSONObject)jsonValue).optJSONObject(HAPManualAdapterDataAssociationForTask.DEFINITION);
		HAPManualDataAssociationForTask dataAssociationForTask = new HAPManualDataAssociationForTask();
		dataAssociationForTask.buildObject(daJsonObj, HAPSerializationFormat.JSON);
		
		entity.setDataAssciation(dataAssociationForTask);
	}
}
