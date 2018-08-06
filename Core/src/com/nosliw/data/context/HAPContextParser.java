package com.nosliw.data.context;

import java.util.Iterator;

import org.json.JSONObject;

import com.nosliw.data.core.criteria.HAPCriteriaParser;

public class HAPContextParser {

	//parse context group
	public static void parseContextGroup(JSONObject contextGroupJson, HAPContextGroup contextGroup) {
		for(String contextType : HAPContextGroup.getContextTypes()){
			JSONObject contextEleJson = contextGroupJson.optJSONObject(contextType);
			HAPContext context = contextGroup.getContext(contextType);
			parseContext(contextEleJson, context);
		}
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
		String name = (String)eleDefJson.opt(HAPContextNodeRootRelative.NAME);
		String description = (String)eleDefJson.opt(HAPContextNodeRootRelative.DESCRIPTION);
		Object defJsonObj = eleDefJson.opt(HAPContextNode.DEFINITION);
		Object defaultJsonObj = eleDefJson.opt(HAPContextNodeRootAbsolute.DEFAULT);
		if(path!=null){
			//relative
			out = new HAPContextNodeRootRelative();
			((HAPContextNodeRootRelative)out).setPath(path);
		}
		else{
			if(defJsonObj!=null)	out = new HAPContextNodeRootAbsolute();   //absolute
			else   out = new HAPContextNodeRootConstant();   //constant
		}
		out.setName(name);
		out.setDescription(description);

		//default value
		if(defaultJsonObj!=null)		out.setDefaultValue(defaultJsonObj);

		//definition
		if(defJsonObj!=null) {
			parseContextNodeFromJson(defJsonObj, out);
		}
		
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
