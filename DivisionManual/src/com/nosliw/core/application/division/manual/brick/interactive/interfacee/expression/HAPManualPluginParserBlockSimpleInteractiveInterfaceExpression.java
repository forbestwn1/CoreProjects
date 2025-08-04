package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition1.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockSimpleInteractiveInterfaceExpression(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, HAPManualDefinitionBlockInteractiveInterfaceExpression.class, manualDivisionEntityMan, runtimeEnv);
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
