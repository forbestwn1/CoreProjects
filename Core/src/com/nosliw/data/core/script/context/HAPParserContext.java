package com.nosliw.data.core.script.context;

import java.util.Iterator;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;

public class HAPParserContext {

	public static HAPContextStructure parseContextStructure(JSONObject jsonObj) {
		HAPContextStructure out = null;
		String type = jsonObj.getString(HAPContextStructure.TYPE);
		if(type.equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_NOTFLAT)) {
			out = parseContextGroup(jsonObj);
		}
		else if(type.equals(HAPConstant.CONTEXTSTRUCTURE_TYPE_FLAT)) {
			out = parseContext(jsonObj);
		}
		return out;
	}
	
	public static HAPContextGroup parseContextGroup(JSONObject contextGroupJson) {
		if(contextGroupJson==null)  return null;
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
			HAPContextDefinitionLeafRelative relativeLeaf = (HAPContextDefinitionLeafRelative)contextRootDef;
			String parent = (String)eleDefJson.opt(HAPContextDefinitionLeafRelative.PARENT);
			relativeLeaf.setParent(parent);
			relativeLeaf.setPath((String)eleDefJson.opt(HAPContextDefinitionLeafRelative.PARENTCATEGARY), path);
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionLeafRelative.DEFINITION);
			if(definitionJsonObj!=null) 	relativeLeaf.setDefinition(parseContextDefinitionElement(definitionJsonObj));
			
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
