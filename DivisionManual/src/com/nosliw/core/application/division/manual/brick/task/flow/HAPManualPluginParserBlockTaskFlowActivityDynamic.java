package com.nosliw.core.application.division.manual.brick.task.flow;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.xxx.application1.HAPAddressValue;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowActivityDynamic;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockTaskFlowActivityDynamic extends HAPManualPluginParserBlockTaskFlowActivity{

	public HAPManualPluginParserBlockTaskFlowActivityDynamic(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYDYNAMIC_100, HAPManualDefinitionBlockTaskFlowActivityDynamic.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {
		super.parseComplexDefinitionContentJson(entityDefinition, jsonObj, parseContext);
		
		HAPManualDefinitionBlockTaskFlowActivityDynamic activityBrick = (HAPManualDefinitionBlockTaskFlowActivityDynamic)entityDefinition;

		
		this.parseBrickAttributeJson(activityBrick, jsonObj, HAPBlockTaskFlowActivityDynamic.TASK, null, null, parseContext);
		
		JSONObject runtimeJson = jsonObj.optJSONObject(HAPBlockTaskFlowActivityDynamic.TASKADDRESS);
		if(runtimeJson!=null) {
			HAPAddressValue runtime = new HAPAddressValue();
			runtime.buildObject(runtimeJson, HAPSerializationFormat.JSON);
			activityBrick.setTaskAddress(runtime);
		}
	}
}
