package com.nosliw.data.core.script.context;

import java.util.Iterator;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.criteria.HAPCriteriaParser;
import com.nosliw.data.core.expression.HAPVariableInfo;

public class HAPParserContext {

	public static HAPContextGroup parseContextGroup(JSONObject contextGroupJson) {
		HAPContextGroup out = new HAPContextGroup();
		parseContextGroup(contextGroupJson, out);
		return out;
	}
	
	//parse context group
	public static void parseContextGroup(JSONObject contextGroupJson, HAPContextGroup contextGroup) {
		JSONObject groupJson = contextGroupJson.optJSONObject(HAPContextGroup.GROUP);
		if(groupJson!=null) {
			for(String contextType : HAPContextGroup.getAllContextTypes()){
				JSONObject contextEleJson = groupJson.optJSONObject(contextType);
				HAPContext context = contextGroup.getContext(contextType);
				parseContext(contextEleJson, context);
			}
		}
		contextGroup.getInfo().buildObject(contextGroupJson.opt(HAPContextGroup.INFO), HAPSerializationFormat.JSON);
	}

	public static HAPContext parseContext(JSONObject contextJson) {
		HAPContext out = new HAPContext();
		parseContext(contextJson, out);
		return out;
	}
	
	public static void parseContext(JSONObject contextJson, HAPContext context) {
		if(contextJson!=null) {
			JSONObject elementsJson = contextJson.optJSONObject(HAPContext.ELEMENT);
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPContextDefinitionRoot contextDefRoot = parseContextRootFromJson(eleDefJson);
				context.addElement(eleName, contextDefRoot);
			}
			
			JSONObject infoJson = contextJson.optJSONObject(HAPContext.INFO);
			if(infoJson!=null) {
				context.getInfo().buildObject(infoJson, HAPSerializationFormat.JSON);
			}
		}
	}
	
	//parse context root
	private static HAPContextDefinitionRoot parseContextRootFromJson(JSONObject eleDefJson){
		HAPContextDefinitionRoot out = new HAPContextDefinitionRoot();

		//info
		HAPEntityInfoImp info = new HAPEntityInfoImp();
		info.buildObject(eleDefJson, HAPSerializationFormat.JSON);
		info.cloneToEntityInfo(out);
		
		//definition
		HAPContextDefinitionElement contextRootDef = parseContextDefinitionElement(eleDefJson.optJSONObject(HAPContextDefinitionRoot.DEFINITION));
		out.setDefinition(contextRootDef);
		return out;
	}
	
	public static HAPContextDefinitionElement parseContextDefinitionElement(JSONObject eleDefJson) {
		HAPContextDefinitionElement contextRootDef = null;
		
		String path = (String)eleDefJson.opt(HAPContextDefinitionLeafRelative.PATH);
		String criteriaStr = (String)eleDefJson.opt(HAPContextDefinitionLeafData.CRITERIA);
		Object valueJsonObj = eleDefJson.opt(HAPContextDefinitionLeafConstant.VALUE);
		JSONObject childrenJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionNode.CHILD);
		Object defaultJsonObj = eleDefJson.opt(HAPContextDefinitionLeafVariable.DEFAULT);
		
		if(path!=null){
			//relative
			contextRootDef = new HAPContextDefinitionLeafRelative();
			((HAPContextDefinitionLeafRelative)contextRootDef).setPath((String)eleDefJson.opt(HAPContextDefinitionLeafRelative.PARENTCATEGARY), path);
			if(defaultJsonObj!=null)		((HAPContextDefinitionLeafRelative)contextRootDef).setDefaultValue(defaultJsonObj);
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionLeafRelative.DEFINITION);
			if(definitionJsonObj!=null)   ((HAPContextDefinitionLeafRelative)contextRootDef).setDefinition(parseContextDefinitionElement(definitionJsonObj));
			((HAPContextDefinitionLeafVariable)contextRootDef).setDefaultValue(defaultJsonObj);
		}
		else if(criteriaStr!=null) {
			//data
			contextRootDef = new HAPContextDefinitionLeafData();   
			//default value
			((HAPContextDefinitionLeafVariable)contextRootDef).setDefaultValue(defaultJsonObj);
			HAPVariableInfo criteria = new HAPVariableInfo();
			criteria.buildObject(criteriaStr, HAPSerializationFormat.LITERATE);
			((HAPContextDefinitionLeafData)contextRootDef).setCriteria(criteria);
		}
		else if(childrenJsonObj!=null) {
			//node
			contextRootDef = new HAPContextDefinitionNode();
			for(Object key : childrenJsonObj.keySet()) {
				((HAPContextDefinitionNode)contextRootDef).addChild((String)key, parseContextDefinitionElement(childrenJsonObj.getJSONObject((String)key)));
			}
		}
		else if(valueJsonObj!=null){
			//constant
			contextRootDef = new HAPContextDefinitionLeafConstant();
			((HAPContextDefinitionLeafConstant)contextRootDef).setValue(valueJsonObj);
		}
		else {
			//value
			contextRootDef = new HAPContextDefinitionLeafValue();
			((HAPContextDefinitionLeafVariable)contextRootDef).setDefaultValue(defaultJsonObj);
		}
		return contextRootDef;
	}
	

	public static void parseContextNodeFromJson(Object json, HAPContextNode contextNode){
		if(json instanceof String){
			contextNode.setDefinition(new HAPContextNodeCriteria(HAPCriteriaParser.getInstance().parseCriteria((String)json)));
		}
		else if(json instanceof JSONObject){
			JSONObject childrenObj = (JSONObject)json;
			Iterator<String> names = childrenObj.keys();
			while(names.hasNext()){
				String name = names.next();
				HAPContextNode childNode = new HAPContextNode();
				parseContextNodeFromJson(childrenObj.opt(name), childNode);
				contextNode.addChild(name, childNode);
			}
		}
	}
}
