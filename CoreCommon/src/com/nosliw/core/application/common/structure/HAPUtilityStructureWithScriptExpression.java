package com.nosliw.core.application.common.structure;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.core.application.common.scriptexpressio.HAPUtilityScriptExpressionConstant;
import com.nosliw.core.application.common.scriptexpressio.HAPUtilityScriptExpressionParser;
import com.nosliw.core.application.common.scriptexpressio.definition.HAPDefinitionContainerScriptExpression;

public class HAPUtilityStructureWithScriptExpression {

	public static void discoverConstantScript(HAPValueStructure valueStructure, HAPDefinitionContainerScriptExpression scriptExpressionContainer) {
		for(HAPRootInStructure root : valueStructure.getRoots().values()) {
			//root name
			String name = root.getName();
			if(HAPUtilityScriptExpressionParser.isScriptExpression(name)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(name);
				root.setName(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			//root id 
			String id = root.getId();
			if(HAPUtilityScriptExpressionParser.isScriptExpression(id)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(id);
				root.setId(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			//root status
			String rootStatus = root.getStatus();
			if(HAPUtilityScriptExpressionParser.isScriptExpression(rootStatus)) {
				String scriptExpressionId = scriptExpressionContainer.addScriptExpression(rootStatus);
				root.setStatus(HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
			}
			
			//relative path
			if(root.getDefinition() instanceof HAPElementStructureLeafRelative) {
				HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)root.getDefinition();
				String relativePath = relativeEle.getReference().getElementPath();
				if(HAPUtilityScriptExpressionParser.isScriptExpression(relativePath)) {
					String relativePathId = scriptExpressionContainer.addScriptExpression(relativePath);
					relativeEle.getReference().setElementPath(HAPUtilityScriptExpressionConstant.makeIdLiterate(relativePathId));
				}
			}
			
			//init value
			Object initValue = valueStructure.getInitValue();
			if(initValue!=null) {
				Map<String, Object> initValueMap = new HashMap<String, Object>();
				
				if(initValue instanceof JSONObject) {
					JSONObject initValueJson = (JSONObject)initValue;
					for(Object key : initValueJson.keySet()) {
						initValueMap.put((String)key, initValueJson.get((String)key));
					}
				}
				else if(initValue instanceof Map) {
					initValueMap = (Map)initValue;
				}
				
				Map<String, String> keyReplace = new HashMap<String, String>();
				for(String key : initValueMap.keySet()) {
					if(HAPUtilityScriptExpressionParser.isScriptExpression(key)) {
						String scriptExpressionId = scriptExpressionContainer.addScriptExpression(key);
						keyReplace.put(key, HAPUtilityScriptExpressionConstant.makeIdLiterate(scriptExpressionId));
					}
				}
				
				//replace key with expresion id
				for(String key : keyReplace.keySet()) {
					initValueMap.put(keyReplace.get(key), initValueMap.remove(key));
				}
				
				valueStructure.setInitValue(initValueMap);
			}
		}
	}
	
	public static void solidateConstantScript(HAPValueStructure valueStructure, Map<String, Object> values) {
		for(HAPRootInStructure root : valueStructure.getRoots().values()) {
			//root name
			String name = root.getName();
			String nameId = HAPUtilityScriptExpressionConstant.isIdLterate(name);
			if(nameId!=null) {
				root.setName(values.get(nameId)+"");
			}

			//root id
			String id = root.getId();
			String idId = HAPUtilityScriptExpressionConstant.isIdLterate(name);
			if(idId!=null) {
				root.setId(values.get(idId)+"");
			}

			//root status
			{
				String rootStatus = root.getStatus();
				String statusId = HAPUtilityScriptExpressionConstant.isIdLterate(rootStatus);
				if(statusId!=null) {
					root.setStatus(values.get(statusId)+"");
				}
			}

			//relative path
			if(root.getDefinition() instanceof HAPElementStructureLeafRelative) {
				HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)root.getDefinition();
				String relativePath = relativeEle.getReference().getElementPath();
				String relativePathId = HAPUtilityScriptExpressionConstant.isIdLterate(relativePath);
				if(relativePathId!=null) {
					relativeEle.getReference().setElementPath(values.get(relativePathId)+"");
				}
			}
			
			//init value
			Map<String, Object> initValue = (Map<String, Object>)valueStructure.getInitValue();
			if(initValue!=null) {
				Map<String, String> keyReplace = new HashMap<String, String>();
				for(String key : initValue.keySet()) {
					String dataExpressionId = HAPUtilityScriptExpressionConstant.isIdLterate(key);
					if(dataExpressionId!=null) {
						keyReplace.put(key, values.get(dataExpressionId)+"");
					}
				}
				for(String key : keyReplace.keySet()) {
					initValue.put(keyReplace.get(key), initValue.remove(key));
				}
			}
		}
	}


}
