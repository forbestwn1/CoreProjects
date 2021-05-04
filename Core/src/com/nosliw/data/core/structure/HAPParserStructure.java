package com.nosliw.data.core.structure;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;

public class HAPParserStructure {

	
	//parse context root
	public static HAPRoot parseContextRootFromJson(JSONObject eleDefJson){
		HAPRoot out = new HAPRoot();

		//info
		out.buildEntityInfoByJson(eleDefJson);

		//local id
		out.setLocalId((String)eleDefJson.opt(HAPRoot.LOCALID));
		
		//global id
		out.setGlobalId((String)eleDefJson.opt(HAPRoot.GLOBALID));
		
		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPRoot.DEFINITION);
		if(defJsonObj!=null)  out.setDefinition(parseContextDefinitionElement(defJsonObj));
		else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementLeafData());
		}
		Object defaultJsonObj = eleDefJson.opt(HAPRoot.DEFAULT);
		out.setDefaultValue(defaultJsonObj);
		return out;
	}
	
	public static HAPElement parseContextDefinitionElement(JSONObject eleDefJson) {
		HAPElement contextRootDef = null;
		
		Object pathObj = eleDefJson.opt(HAPElementLeafRelative.PATH);
		Object criteriaDef = eleDefJson.opt(HAPElementLeafData.CRITERIA);
		Object valueJsonObj = eleDefJson.opt(HAPElementLeafConstant.VALUE);
		JSONObject childrenJsonObj = eleDefJson.optJSONObject(HAPElementNode.CHILD);
		String constantName = (String)eleDefJson.opt(HAPElementLeafConstantReference.CONSTANT);
		
		if(pathObj!=null){
			//relative
			contextRootDef = new HAPElementLeafRelative();
			HAPElementLeafRelative relativeLeaf = (HAPElementLeafRelative)contextRootDef;
			String parent = (String)eleDefJson.opt(HAPElementLeafRelative.PARENT);
			relativeLeaf.setParent(parent);
			if(pathObj instanceof String)	relativeLeaf.setReferencePath((String)pathObj);
			else if(pathObj instanceof JSONObject){
				HAPReferenceElement contextPath = HAPUtilityStructurePath.parseJsonStructurePath((JSONObject)pathObj); 
				relativeLeaf.setResolvedPath(contextPath);
			}
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPElementLeafRelative.DEFINITION);
			if(definitionJsonObj!=null) 	relativeLeaf.setDefinition(parseContextDefinitionElement(definitionJsonObj));
			
			JSONObject solidNodeRefJsonObj = eleDefJson.optJSONObject(HAPElementLeafRelative.SOLIDNODEREF);
			if(solidNodeRefJsonObj!=null) {
				HAPInfoPathToSolidRoot solidNodeRef = new HAPInfoPathToSolidRoot();
				solidNodeRef.buildObject(solidNodeRefJsonObj, HAPSerializationFormat.JSON);
				relativeLeaf.setSolidNodeReference(solidNodeRef);
			}
		}
		else if(criteriaDef!=null) {
			//data
			HAPVariableDataInfo dataInfo = new HAPVariableDataInfo();
			dataInfo.buildObject(criteriaDef, null);
			contextRootDef = new HAPElementLeafData(dataInfo);   
		}
		else if(childrenJsonObj!=null) {
			//node
			contextRootDef = new HAPElementNode();
			for(Object key : childrenJsonObj.keySet()) {
				((HAPElementNode)contextRootDef).addChild((String)key, parseContextDefinitionElement(childrenJsonObj.getJSONObject((String)key)));
			}
		}
		else if(valueJsonObj!=null){
			//constant
			contextRootDef = new HAPElementLeafConstant();
			((HAPElementLeafConstant)contextRootDef).setValue(valueJsonObj);
		}
		else if(constantName!=null) {
			//constant reference
			contextRootDef = new HAPElementLeafConstantReference(constantName);
		}
		else {
			//value
			contextRootDef = new HAPElementLeafValue();
		}
		return contextRootDef;
	}

}
