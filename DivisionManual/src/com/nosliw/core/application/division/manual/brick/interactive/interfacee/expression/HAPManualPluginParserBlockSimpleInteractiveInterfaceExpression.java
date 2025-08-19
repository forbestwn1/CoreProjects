package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, HAPManualDefinitionBlockInteractiveInterfaceExpression.class, manualDivisionEntityMan, brickMan);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockInteractiveInterfaceExpression expressionInteractiveDef = (HAPManualDefinitionBlockInteractiveInterfaceExpression)entityDefinition;
		
		JSONObject jsonObj = (JSONObject)jsonValue;
		JSONObject valueJsonObj = jsonObj.optJSONObject(HAPBlockInteractiveInterfaceTask.VALUE);
		if(valueJsonObj==null) {
			valueJsonObj = jsonObj;
		}
		
		HAPInteractiveExpression taskInteractive = new HAPInteractiveExpression();
		taskInteractive.buildObject(valueJsonObj, HAPSerializationFormat.JSON);
		expressionInteractiveDef.setValue(taskInteractive);
	}
}
