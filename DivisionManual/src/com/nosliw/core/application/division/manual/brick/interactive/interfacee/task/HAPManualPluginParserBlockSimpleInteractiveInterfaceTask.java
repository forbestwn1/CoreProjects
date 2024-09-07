package com.nosliw.core.application.division.manual.brick.interactive.interfacee.task;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockSimpleInteractiveInterfaceTask extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockSimpleInteractiveInterfaceTask(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, HAPManualDefinitionBlockInteractiveInterfaceTask.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockInteractiveInterfaceTask taskInteractiveDef = (HAPManualDefinitionBlockInteractiveInterfaceTask)entityDefinition;
		
		JSONObject jsonObj = (JSONObject)jsonValue;
		JSONObject valueJsonObj = jsonObj.optJSONObject(HAPBlockInteractiveInterfaceTask.VALUE);
		if(valueJsonObj==null) {
			valueJsonObj = jsonObj;
		}
		
		HAPInteractiveTask taskInteractive = new HAPInteractiveTask();
		taskInteractive.buildObject(valueJsonObj, HAPSerializationFormat.JSON);
		taskInteractiveDef.setValue(taskInteractive);
	}
}
