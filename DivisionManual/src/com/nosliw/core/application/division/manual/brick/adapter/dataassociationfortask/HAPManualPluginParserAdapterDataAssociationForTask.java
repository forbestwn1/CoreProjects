package com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.common.dataassociation.HAPManualDataAssociationForTask;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition1.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserAdapterDataAssociationForTask  extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserAdapterDataAssociationForTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, HAPManualDefinitionAdapterDataAssociationForTask.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionAdapterDataAssociationForTask entity = (HAPManualDefinitionAdapterDataAssociationForTask)brickManual;
		
		JSONObject daJsonObj =  ((JSONObject)jsonValue).optJSONObject(HAPManualDefinitionAdapterDataAssociationForTask.DEFINITION);
		HAPManualDataAssociationForTask dataAssociationForTask = new HAPManualDataAssociationForTask();
		dataAssociationForTask.buildObject(daJsonObj, HAPSerializationFormat.JSON);
		
		entity.setDataAssciation(dataAssociationForTask);
	}
}
