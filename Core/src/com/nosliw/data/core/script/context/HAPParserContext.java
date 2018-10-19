package com.nosliw.data.core.script.context;

import java.util.Iterator;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.criteria.HAPCriteriaParser;

public class HAPParserContext {

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
	
	public static void parseContext(JSONObject contextJson, HAPContext context) {
		if(contextJson!=null) {
			JSONObject elementsJson = contextJson.optJSONObject(HAPContext.ELEMENT);
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPContextNodeRoot tagDefContextEle = parseContextRootFromJson(eleDefJson);
				context.addElement(eleName, tagDefContextEle);
			}
			
			JSONObject infoJson = contextJson.optJSONObject(HAPContext.INFO);
			if(infoJson!=null) {
				context.getInfo().buildObject(infoJson, HAPSerializationFormat.JSON);
			}
		}
	}
	
	//parse context root
	private static HAPContextNodeRoot parseContextRootFromJson(JSONObject eleDefJson){
		HAPContextNodeRoot out = null;
		String path = (String)eleDefJson.opt(HAPContextNodeRootRelative.PATH);
		Object defJsonObj = eleDefJson.opt(HAPContextNode.DEFINITION);
		Object defaultJsonObj = eleDefJson.opt(HAPContextNodeRootVariable.DEFAULT);
		
		HAPEntityInfoImp info = new HAPEntityInfoImp();
		info.buildObject(eleDefJson, HAPSerializationFormat.JSON);
		
		if(path!=null){
			//relative
			out = new HAPContextNodeRootRelative();
			((HAPContextNodeRootRelative)out).setPath((String)eleDefJson.opt(HAPContextNodeRootRelative.PARENTCATEGARY), path);
			if(defaultJsonObj!=null)		((HAPContextNodeRootRelative)out).setDefaultValue(defaultJsonObj);
			if(defJsonObj!=null) 	parseContextNodeFromJson(defJsonObj, (HAPContextNodeRootRelative)out);
			((HAPContextNodeRootRelative)out).setInfo(info);
		}
		else if(defJsonObj!=null) {
			//absolute
			out = new HAPContextNodeRootAbsolute();   
			//default value
			if(defaultJsonObj!=null)		((HAPContextNodeRootAbsolute)out).setDefaultValue(defaultJsonObj);
			if(defJsonObj!=null) 	parseContextNodeFromJson(defJsonObj, (HAPContextNodeRootAbsolute)out);
			((HAPContextNodeRootAbsolute)out).setInfo(info);
		}
		else{
			//constant
			out = new HAPContextNodeRootConstant(eleDefJson.opt(HAPContextNodeRootConstant.VALUE));
			info.cloneToEntityInfo((HAPContextNodeRootConstant)out);
		}
		
		out.getInfo().buildObject(eleDefJson, HAPSerializationFormat.JSON);
		
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
