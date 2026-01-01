package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression extends HAPManualDefinitionPluginParserBrickImpSimple{

	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerDataRule dataRuleMan) {
		super(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, HAPManualDefinitionBlockInteractiveInterfaceExpression.class, manualDivisionEntityMan, brickMan);
		this.m_dataRuleMan = dataRuleMan;
	}
	
	@Override
	protected void parseSimpleDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockInteractiveInterfaceExpression expressionInteractiveDef = (HAPManualDefinitionBlockInteractiveInterfaceExpression)entityDefinition;
		
		JSONObject jsonObj = (JSONObject)jsonValue;
		JSONObject valueJsonObj = jsonObj.optJSONObject(HAPBlockInteractiveInterfaceTask.VALUE);
		if(valueJsonObj==null) {
			valueJsonObj = jsonObj;
		}
		
		HAPInteractiveExpression taskInteractive = HAPInteractiveExpression.parse(valueJsonObj, m_dataRuleMan); 
		expressionInteractiveDef.setValue(taskInteractive);
	}
}
