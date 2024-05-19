package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociation.HAPManualAdapterDataAssociation;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForTask;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserAdapterDataAssociationForTask  extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserAdapterDataAssociationForTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, HAPManualAdapterDataAssociationForTask.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick brickManual, Object jsonValue, HAPManualContextParse parseContext) {
		HAPManualAdapterDataAssociationForTask entity = (HAPManualAdapterDataAssociationForTask)brickManual;
		
		JSONObject daJsonObj =  ((JSONObject)jsonValue).optJSONObject(HAPManualAdapterDataAssociation.DEFINITION);
		HAPManualDataAssociationForTask dataAssociationForTask = new HAPManualDataAssociationForTask();
		dataAssociationForTask.buildObject(daJsonObj, HAPSerializationFormat.JSON);
		
		entity.setDataAssciation(dataAssociationForTask);
	}
}
