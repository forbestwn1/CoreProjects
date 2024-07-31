package com.nosliw.core.application.division.manual.common.scriptexpression;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.constant.HAPDefinitionConstant;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrickTraverse;
import com.nosliw.data.core.domain.entity.expression.script.HAPUtilityScriptExpressionDefinition;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.imp.rhino.task.HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup;

public class HAPManualUtilityScriptExpressionConstant {

	private static String SYMBLE_IDLITERATE_START = "{{"; 
	private static String SYMBLE_IDLITERATE_END = "}}"; 
	
	public static String discoverConstantScript(String content, HAPWithScriptExpressionConstantMaster withConstantScriptExpression) {
		String out = null;
		if(HAPUtilityScriptExpressionDefinition.isScriptExpression(content)) {
			out = withConstantScriptExpression.getScriptExpressionConstantContainer().addScriptExpression(content);
		}
		return out;
	}

	public static String makeIdLiterate(String id) {
		return SYMBLE_IDLITERATE_START + id + SYMBLE_IDLITERATE_END;
	}
	
	public static String isIdLterate(String content) {
		String out = null;
		if(content.startsWith(SYMBLE_IDLITERATE_START) && content.endsWith(SYMBLE_IDLITERATE_END)) {
			out = content.substring(SYMBLE_IDLITERATE_START.length(), content.length()-SYMBLE_IDLITERATE_END.length());
		}
		return out;
	}
	
	
	public static void discoverScriptExpressionConstantInBrick(HAPManualDefinitionBrick brickDef) {
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrick(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithBrick() {

			@Override
			protected boolean processBrick(HAPManualDefinitionBrick brick, Object data) {
				if(brick instanceof HAPWithScriptExpressionConstant) {
					HAPWithScriptExpressionConstantMaster withScriptExpressionConstant = brickDef;
					withScriptExpressionConstant.discoverConstantScript();
				}
				return true;
			}
			
		}, brickDef);
	}

	public static Map<String, Map<String, Object>> calculateScriptExpressionConstants(HAPManualDefinitionBrick brickDef, HAPRuntimeEnvironment runtTimeEnv) {
		HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup taskInfo = new HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup();
		
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrick(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {
			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				HAPManualDefinitionBrick brick = HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(rootBrick, path);
				
				if(brick instanceof HAPWithScriptExpressionConstantMaster) {
					HAPWithScriptExpressionConstantMaster withScriptExpressionConstant = brick;
					HAPManualDefinitionContainerScriptExpression containerEle = withScriptExpressionConstant.getScriptExpressionConstantContainer();
					for(HAPManualDefinitionScriptExpressionItemInContainer item :  containerEle.getItems()) {
						Map<String, HAPDefinitionConstant> constantsDef = brick.getConstantDefinitions();
						Map<String, Object> constants = new LinkedHashMap<String, Object>();
						for(String name : constantsDef.keySet()) {
							constants.put(name, constantsDef.get(name).getValue());
						}
						
						HAPManualExpressionScript scriptExpression = processScriptExpressionConstant((HAPManualDefinitionScriptExpressionConstant)item.getValue(), constantsDef, runtTimeEnv.getDataExpressionParser());
						String itemName = null;
						if(path==null||path.isEmpty()) {
							itemName = item.getName();
						} else {
							HAPUtilityNamingConversion.cascadePath(path.getPath(), item.getName());
						}
						taskInfo.addScriptExpressionInfo(itemName, scriptExpression, constants);
					}
				}
				return true;
			}
			
		}, null);

		HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(taskInfo, runtTimeEnv);

		HAPServiceData serviceData = runtTimeEnv.getRuntime().executeTaskSync(task);
		JSONObject serviceDataJson = (JSONObject)serviceData.getData();
		
		Map<String, Map<String, Object>> out = new LinkedHashMap<String, Map<String, Object>>();
		for(Object key : serviceDataJson.keySet()) {
			String keyStr = (String)key;
			Pair<HAPPath, String> pair = new HAPPath(keyStr).trimLast();
			String brickPath = pair.getLeft().isEmpty()?"":pair.getLeft().getPath();
			Map<String, Object> byBrick = out.get(brickPath);
			if(byBrick==null) {
				byBrick = new LinkedHashMap<String, Object>();
				out.put(brickPath, byBrick);
			}
			byBrick.put(pair.getRight(), out);
		}
		
		return out;
	}

	
	public static HAPManualDefinitionContainerScriptExpression solidateScriptExpressionConstantInBrick(HAPManualDefinitionBrick brickDef, Map<String, Map<String, Object>> constants) {
		HAPManualDefinitionContainerScriptExpression out = new HAPManualDefinitionContainerScriptExpression();
		HAPManualDefinitionUtilityBrickTraverse.traverseBrickTreeLeavesOfBrick(brickDef, null, new HAPManualDefinitionProcessorBrickNodeDownwardWithPath() {

			@Override
			public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
				if(brickDef instanceof HAPWithScriptExpressionConstantMaster) {
					HAPWithScriptExpressionConstantMaster withScriptExpressionConstant = brickDef;
					String brickPath = path==null||path.isEmpty()?"":path.getPath();
					withScriptExpressionConstant.solidateConstantScript(constants.get(brickPath));
				}
				return true;
			}
			
		}, null);
		return out;
	}
	
	public static HAPManualExpressionScript processScriptExpressionConstant(HAPManualDefinitionScriptExpressionConstant scriptExpressionDef, Map<String, HAPDefinitionConstant> constantsDef, HAPParserDataExpression dataExpressionParser) {
		
		HAPManualExpressionScript scriptExpression = HAPManualUtilityScriptExpressionParser.parseDefinitionExpression(scriptExpressionDef.getScriptExpression(), scriptExpressionDef.getScriptExpressionType(), dataExpressionParser);

		HAPManualUtilityScriptExpression.processScriptExpressionConstant(scriptExpression, constantsDef);

		return scriptExpression;
	}

	public static HAPServiceData executeScriptExpressionConstant(HAPManualDefinitionScriptExpressionConstant scriptExpressionDef, Map<String, Object> constants, HAPRuntimeEnvironmentImpRhino runtimeEnvironment) {
		Map<String, HAPDefinitionConstant> constantsDef = new LinkedHashMap<String, HAPDefinitionConstant>();
		for(String name : constants.keySet()) {
			constantsDef.put(name, new HAPDefinitionConstant(name, constants.get(name)));
		}
		
		HAPManualExpressionScript scriptExpression = processScriptExpressionConstant(scriptExpressionDef, constantsDef, runtimeEnvironment.getDataExpressionParser());
		
		HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup taskInfo = new HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup();
		taskInfo.addScriptExpressionInfo(HAPConstantShared.NAME_DEFAULT, scriptExpression, constants);
		
		HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup task = new HAPRuntimeTaskExecuteRhinoScriptExpressionConstantGroup(taskInfo, runtimeEnvironment);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		return out;
	}
	
}
