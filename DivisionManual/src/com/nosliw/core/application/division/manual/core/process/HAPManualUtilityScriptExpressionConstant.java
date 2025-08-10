package com.nosliw.core.application.division.manual.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionContainerScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionScriptExpression;
import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionScriptExpressionItemInContainer;
import com.nosliw.core.application.common.scriptexpression.HAPManualExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPUtilityScriptExpressionConstant;
import com.nosliw.core.application.common.scriptexpression.HAPWithScriptExpressionConstantMaster;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrickTraverse;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;
import com.nosliw.core.runtimeenv.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.core.xxx.application.division.manual.core.definition1.HAPManualDefinitionProcessorBrickNodeDownwardWithBrick;

public class HAPManualUtilityScriptExpressionConstant {

	public static String discoverConstantScript(String content, HAPWithScriptExpressionConstantMaster withConstantScriptExpression) {
		String out = null;
		if(HAPUtilityScriptExpressionDefinition.isScriptExpression(content)) {
			out = withConstantScriptExpression.getScriptExpressionConstantContainer().addScriptExpression(content);
		}
		return out;
	}

	public static void discoverScriptExpressionConstantInBrick(HAPManualDefinitionBrick brickDef, HAPManualManagerBrick manualBrickMan) {
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrickComplex(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithBrick() {

			@Override
			protected boolean processBrick(HAPManualDefinitionBrick brick, Object data) {
				if(brick instanceof HAPWithScriptExpressionConstantMaster) {
					HAPWithScriptExpressionConstantMaster withScriptExpressionConstant = brick;
					withScriptExpressionConstant.discoverConstantScript();
				}
				return true;
			}
			
		}, manualBrickMan, brickDef);
	}

	public static Map<String, Map<String, Object>> calculateScriptExpressionConstants(HAPManualDefinitionBrick brickDef, HAPRuntimeEnvironment runtTimeEnv, HAPManualManagerBrick manualBrickMan) {
		HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup taskInfo = new HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup();
		
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrickComplex(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {
			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				HAPManualDefinitionBrick brick = HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(rootBrick, path);
				
				if(brick instanceof HAPWithScriptExpressionConstantMaster) {
					HAPWithScriptExpressionConstantMaster withScriptExpressionConstant = brick;
					HAPDefinitionContainerScriptExpression containerEle = withScriptExpressionConstant.getScriptExpressionConstantContainer();
					for(HAPDefinitionScriptExpressionItemInContainer item :  containerEle.getItems()) {
						Map<String, HAPDefinitionConstant> constantsDef = brick.getConstantDefinitions();
						Map<String, Object> constants = new LinkedHashMap<String, Object>();
						for(String name : constantsDef.keySet()) {
							constants.put(name, constantsDef.get(name).getValue());
						}
						
						HAPManualExpressionScript scriptExpression = HAPUtilityScriptExpressionConstant.processScriptExpressionConstant((HAPDefinitionScriptExpression)item.getValue(), constantsDef, runtTimeEnv.getDataExpressionParser());
						String itemName = null;
						if(path==null||path.isEmpty()) {
							itemName = item.getName();
						} else {
							itemName = HAPUtilityNamingConversion.cascadePath(path.getPath(), item.getName());
						}
						taskInfo.addScriptExpressionInfo(itemName, scriptExpression, constants);
					}
				}
				return true;
			}
			
		}, manualBrickMan, null);

		
		
		Map<String, Map<String, Object>> out = new LinkedHashMap<String, Map<String, Object>>();
		if(!taskInfo.isEmpty()) {
			HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(taskInfo, runtTimeEnv);

			HAPServiceData serviceData = runtTimeEnv.getRuntime().executeTaskSync(task);
			JSONObject serviceDataJson = (JSONObject)serviceData.getData();
			
			for(Object key : serviceDataJson.keySet()) {
				String keyStr = (String)key;
				Pair<HAPPath, String> pair = new HAPPath(keyStr).trimLast();
				String brickPath = pair.getLeft().isEmpty()?"":pair.getLeft().getPath();
				Map<String, Object> byBrick = out.get(brickPath);
				if(byBrick==null) {
					byBrick = new LinkedHashMap<String, Object>();
					out.put(brickPath, byBrick);
				}
				byBrick.put(pair.getRight(), serviceDataJson.get((String)key));
			}
		}
		
		return out;
	}

	
	public static HAPDefinitionContainerScriptExpression solidateScriptExpressionConstantInBrick(HAPManualDefinitionBrick brickDef, Map<String, Map<String, Object>> constants, HAPManualManagerBrick manualBrickMan) {
		HAPDefinitionContainerScriptExpression out = new HAPDefinitionContainerScriptExpression();
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrickComplex(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				HAPManualDefinitionBrick brick = HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(rootBrick, path);
				if(brick instanceof HAPWithScriptExpressionConstantMaster) {
					HAPWithScriptExpressionConstantMaster withScriptExpressionConstant = brick;
					String brickPath = path==null||path.isEmpty()?"":path.getPath();
					withScriptExpressionConstant.solidateConstantScript(constants.get(brickPath));
				}
				return true;
			}
			
		}, manualBrickMan, null);
		return out;
	}
	
	public static HAPServiceData executeScriptExpressionConstant(HAPDefinitionScriptExpression scriptExpressionDef, Map<String, Object> constants, HAPRuntimeEnvironmentImpRhino runtimeEnvironment) {
		Map<String, HAPDefinitionConstant> constantsDef = new LinkedHashMap<String, HAPDefinitionConstant>();
		for(String name : constants.keySet()) {
			constantsDef.put(name, new HAPDefinitionConstant(name, constants.get(name)));
		}
		
		HAPManualExpressionScript scriptExpression = HAPUtilityScriptExpressionConstant.processScriptExpressionConstant(scriptExpressionDef, constantsDef, runtimeEnvironment.getDataExpressionParser());
		
		HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup taskInfo = new HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup();
		taskInfo.addScriptExpressionInfo(HAPConstantShared.NAME_DEFAULT, scriptExpression, constants);
		
		HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(taskInfo, runtimeEnvironment);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		return out;
	}
	
}
