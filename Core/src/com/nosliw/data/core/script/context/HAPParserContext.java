package com.nosliw.data.core.script.context;

import java.util.Iterator;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
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
		}
	}
	
	//parse context root
	private static HAPContextDefinitionRoot parseContextRootFromJson(JSONObject eleDefJson){
		HAPContextDefinitionRoot out = new HAPContextDefinitionRoot();

		//info
		HAPEntityInfoWritableImp info = new HAPEntityInfoWritableImp();
		info.buildObject(eleDefJson, HAPSerializationFormat.JSON);
		info.cloneToEntityInfo(out);
		
		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionRoot.DEFINITION);
		if(defJsonObj!=null)  out.setDefinition(parseContextDefinitionElement(defJsonObj));
		else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPContextDefinitionLeafData(HAPVariableInfo.buildUndefinedVariableInfo()));
		}
		Object defaultJsonObj = eleDefJson.opt(HAPContextDefinitionRoot.DEFAULT);
		out.setDefaultValue(defaultJsonObj);
		return out;
	}
	
	public static HAPContextDefinitionElement parseContextDefinitionElement(JSONObject eleDefJson) {
		HAPContextDefinitionElement contextRootDef = null;
		
		String path = (String)eleDefJson.opt(HAPContextDefinitionLeafRelative.PATH);
		Object criteriaDef = eleDefJson.opt(HAPContextDefinitionLeafData.CRITERIA);
		Object valueJsonObj = eleDefJson.opt(HAPContextDefinitionLeafConstant.VALUE);
		JSONObject childrenJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionNode.CHILD);
		
		if(path!=null){
			//relative
			contextRootDef = new HAPContextDefinitionLeafRelative();
			((HAPContextDefinitionLeafRelative)contextRootDef).setPath((String)eleDefJson.opt(HAPContextDefinitionLeafRelative.PARENTCATEGARY), path);
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionLeafRelative.DEFINITION);
		}
		else if(criteriaDef!=null) {
			//data
			HAPVariableInfo criteria = HAPVariableInfo.buildVariableInfoFromObject(criteriaDef);
			contextRootDef = new HAPContextDefinitionLeafData(criteria);   
		}
		else if(childrenJsonObj!=null) {
			//node
			contextRootDef = new HAPContextDefinitionNode();
			for(Object key : childrenJsonObj.keySet()) {
				((HAPContextDefinitionNode)contextRootDef).addChild((String)key, parseContextDefinitionElement(childrenJsonObj.getJSONObject((String)key)));
			}
			//default value
		}
		else if(valueJsonObj!=null){
			//constant
			contextRootDef = new HAPContextDefinitionLeafConstant();
			((HAPContextDefinitionLeafConstant)contextRootDef).setValue(valueJsonObj);
		}
		else {
			//value
			contextRootDef = new HAPContextDefinitionLeafValue();
		}
		return contextRootDef;
	}
	
}
