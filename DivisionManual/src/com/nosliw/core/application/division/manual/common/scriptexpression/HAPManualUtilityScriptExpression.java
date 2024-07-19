package com.nosliw.core.application.division.manual.common.scriptexpression;

import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.common.scriptexpression.HAPContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpression.HAPItemInContainerScriptExpression;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;

public class HAPManualUtilityScriptExpression {

	public static void fromDefToExeScriptExpressionContainer(HAPManualDefinitionContainerScriptExpression groupDef, HAPContainerScriptExpression groupExe, HAPParserDataExpression dataExpressionParser) {
		for(HAPManualDefinitionScriptExpressionItemInContainer itemDef : groupDef.getItems()) {
			HAPItemInContainerScriptExpression itemExe = new HAPItemInContainerScriptExpression();
			itemDef.cloneToEntityInfo(itemExe);

			HAPManualExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(itemDef.getScriptExpression(), null, dataExpressionParser);
			itemExe.setScriptExpression(scriptExpression);
			groupExe.addItem(itemExe);
		}
	}

	//variable resolve in script expression container
	public static void processScriptExpressionContainerVariableResolve(HAPContainerScriptExpression groupExe, HAPContainerVariableInfo varInfoContainer, HAPConfigureResolveElementReference resolveConfigure, HAPManualManagerBrick manualBrickMan) {
		for(HAPItemInContainerScriptExpression itemExe : groupExe.getItems()) {
			//variable resolve
			HAPUtilityWithVarible.resolveVariable(itemExe.getScriptExpression(), varInfoContainer, resolveConfigure, manualBrickMan);
			//build variable info in script expression
			HAPUtilityWithVarible.buildVariableInfoInEntity(itemExe.getScriptExpression(), varInfoContainer, manualBrickMan);
		}
	}
	
}
