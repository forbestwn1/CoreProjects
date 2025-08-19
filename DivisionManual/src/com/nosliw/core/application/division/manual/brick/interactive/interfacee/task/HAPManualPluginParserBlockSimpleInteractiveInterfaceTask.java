package com.nosliw.core.application.division.manual.brick.interactive.interfacee.task;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBlockSimpleInteractiveInterfaceTask extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockSimpleInteractiveInterfaceTask(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, HAPManualDefinitionBlockInteractiveInterfaceTask.class, manualDivisionEntityMan, brickMan);
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
