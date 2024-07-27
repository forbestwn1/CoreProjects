package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrickTraverse;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoScriptExpressionConstant;

public class HAPManualUtilityScriptExpressionConstant {

	public static void discoverScriptExpressionConstantInBrick(HAPManualDefinitionBrick brickDef) {
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrick(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithBrick() {

			@Override
			protected boolean processBrick(HAPManualDefinitionBrick brick, Object data) {
				if(brickDef instanceof HAPWithScriptExpressionConstant) {
					HAPWithScriptExpressionConstant withScriptExpressionConstant = (HAPWithScriptExpressionConstant)brickDef;
					withScriptExpressionConstant.discoverConstantScript();
				}
				return true;
			}
			
		}, brickDef);
	}
	
	public static HAPManualDefinitionContainerScriptExpression solidateScriptExpressionConstants(HAPManualDefinitionBrick brickDef) {
		HAPManualDefinitionContainerScriptExpression out = new HAPManualDefinitionContainerScriptExpression();
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrick(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				if(brickDef instanceof HAPWithScriptExpressionConstant) {
					HAPWithScriptExpressionConstant withScriptExpressionConstant = (HAPWithScriptExpressionConstant)brickDef;
					HAPManualDefinitionContainerScriptExpression containerEle = withScriptExpressionConstant.getScriptExpressionConstantContainer();
					
				}
				return true;
			}
			
		}, null);
		return out;
	}
	
	public static HAPManualExpressionScript processScriptExpressionConstant(HAPManualDefinitionScriptExpressionConstant scriptExpressionDef, Map<String, HAPDefinitionConstant> constantsDef, HAPParserDataExpression dataExpressionParser) {
		
		HAPManualExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(scriptExpressionDef.getScriptExpression(), scriptExpressionDef.getScriptExpression(), dataExpressionParser);

		HAPManualUtilityScriptExpression.processScriptExpressionConstant(scriptExpression, constantsDef);

		return scriptExpression;
	}

	public static HAPServiceData executeScriptExpressionConstant(HAPManualDefinitionScriptExpressionConstant scriptExpressionDef, Map<String, Object> constants, HAPRuntimeEnvironmentImpRhino runtimeEnvironment) {
		Map<String, HAPDefinitionConstant> constantsDef = new LinkedHashMap<String, HAPDefinitionConstant>();
		for(String name : constants.keySet()) {
			constantsDef.put(name, new HAPDefinitionConstant(name, constants.get(name)));
		}
		
		HAPManualExpressionScript scriptExpression = processScriptExpressionConstant(scriptExpressionDef, constantsDef, runtimeEnvironment.getDataExpressionParser());
		
		HAPRuntimeTaskExecuteRhinoScriptExpressionConstant task = new HAPRuntimeTaskExecuteRhinoScriptExpressionConstant(scriptExpression, constants, runtimeEnvironment);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		return out;
	}
	
}
