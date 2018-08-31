package com.nosliw.data.core.script.context;

import java.util.Iterator;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.criteria.HAPCriteriaParser;

public class HAPContextParser {

	//parse context group
	public static void parseContextGroup(JSONObject contextGroupJson, HAPContextGroup contextGroup) {
		for(String contextType : HAPContextGroup.getAllContextTypes()){
			JSONObject contextEleJson = contextGroupJson.optJSONObject(contextType);
			HAPContext context = contextGroup.getContext(contextType);
			parseContext(contextEleJson, context);
		}
		contextGroup.getInfo().buildObject(contextGroupJson.opt(HAPContextGroup.INFO), HAPSerializationFormat.JSON);
	}
	
	public static void parseContext(JSONObject contextJson, HAPContext context) {
		if(contextJson!=null) {
			Iterator<String> it = contextJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = contextJson.optJSONObject(eleName);
				HAPContextNodeRoot tagDefContextEle = parseContextRootFromJson(eleDefJson);
				context.addElement(eleName, tagDefContextEle);
			}
		}
	}
	
	//parse context root
	private static HAPContextNodeRoot parseContextRootFromJson(JSONObject eleDefJson){
		HAPContextNodeRoot out = null;
		String path = (String)eleDefJson.opt(HAPContextNodeRootRelative.PATH);
		Object defJsonObj = eleDefJson.opt(HAPContextNode.DEFINITION);
		Object defaultJsonObj = eleDefJson.opt(HAPContextNodeRootVariable.DEFAULT);
		
		if(path!=null){
			//relative
			out = new HAPContextNodeRootRelative();
			((HAPContextNodeRootRelative)out).setPath((String)eleDefJson.opt(HAPContextNodeRootRelative.PARENTCATEGARY), path);
			if(defaultJsonObj!=null)		((HAPContextNodeRootRelative)out).setDefaultValue(defaultJsonObj);
			if(defJsonObj!=null) 	parseContextNodeFromJson(defJsonObj, (HAPContextNodeRootRelative)out);
		}
		else if(defJsonObj!=null) {
			//absolute
			out = new HAPContextNodeRootAbsolute();   
			//default value
			if(defaultJsonObj!=null)		((HAPContextNodeRootAbsolute)out).setDefaultValue(defaultJsonObj);
			if(defJsonObj!=null) 	parseContextNodeFromJson(defJsonObj, (HAPContextNodeRootAbsolute)out);
		}
		else{
			//constant
			out = new HAPContextNodeRootConstant();   
			
		}
		String name = (String)eleDefJson.opt(HAPContextNodeRootInfo.DISPLAYNAME);
		out.getInfo().setDisplayName(name);
		String description = (String)eleDefJson.opt(HAPContextNodeRootInfo.DESCRIPTION);
		out.getInfo().setDescription(description);
		out.getInfo().buildObject(eleDefJson.opt(HAPContextNodeRoot.INFO), HAPSerializationFormat.JSON);
		

		//definition
		
		return out;
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
