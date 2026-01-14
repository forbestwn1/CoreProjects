package com.nosliw.core.application.division.manual.brick.interactive.interfacee.task;

import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPManualPluginParserBlockSimpleInteractiveInterfaceTask extends HAPManualDefinitionPluginParserBrickImpSimple{

	HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualPluginParserBlockSimpleInteractiveInterfaceTask(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerDataRule dataRuleMan) {
		super(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, HAPManualDefinitionBlockInteractiveInterfaceTask.class, manualDivisionEntityMan, brickMan);
		this.m_dataRuleMan = dataRuleMan;
	}
	
	@Override
	protected void parseSimpleDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockInteractiveInterfaceTask taskInteractiveDef = (HAPManualDefinitionBlockInteractiveInterfaceTask)entityDefinition;
		
		JSONObject jsonObj = (JSONObject)jsonValue;
		JSONObject valueJsonObj = jsonObj.optJSONObject(HAPBlockInteractiveInterfaceTask.VALUE);
		if(valueJsonObj==null) {
			valueJsonObj = jsonObj;
		}
		
		HAPInteractiveTask taskInteractive = HAPInteractiveTask.parse(valueJsonObj, this.m_dataRuleMan);
		taskInteractiveDef.setValue(taskInteractive);
	}
}
