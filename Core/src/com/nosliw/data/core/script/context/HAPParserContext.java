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
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionRoot.DEFINITION);
		if(defJsonObj!=null)  out.setDefinition(parseContextDefinitionElement(defJsonObj));
		else out.setDefinition(new HAPContextDefinitionUnknown());
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
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPContextDefinitionLeafRelative.DEFINITION);
			if(definitionJsonObj!=null)   ((HAPContextDefinitionLeafRelative)contextRootDef).setDefinition(parseContextDefinitionElement(definitionJsonObj));
			((HAPContextDefinitionLeafVariable)contextRootDef).setDefaultValue(defaultJsonObj);
		}
		else if(criteriaStr!=null) {
			//data
			contextRootDef = new HAPContextDefinitionLeafData();   
			HAPVariableInfo criteria = new HAPVariableInfo();
			criteria.buildObject(criteriaStr, HAPSerializationFormat.LITERATE);
			((HAPContextDefinitionLeafData)contextRootDef).setCriteria(criteria);
			//default value
			((HAPContextDefinitionLeafVariable)contextRootDef).setDefaultValue(defaultJsonObj);
		}
		else if(childrenJsonObj!=null) {
			//node
			contextRootDef = new HAPContextDefinitionNode();
			for(Object key : childrenJsonObj.keySet()) {
				((HAPContextDefinitionNode)contextRootDef).addChild((String)key, parseContextDefinitionElement(childrenJsonObj.getJSONObject((String)key)));
			}
			//default value
			((HAPContextDefinitionLeafVariable)contextRootDef).setDefaultValue(defaultJsonObj);
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
	
}
