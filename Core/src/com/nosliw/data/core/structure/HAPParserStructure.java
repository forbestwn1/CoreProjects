package com.nosliw.data.core.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.structure.reference.HAPInfoPathReference;

public class HAPParserStructure {

	public static List<HAPRootStructure> parseRoots(Object rootsObj){
		List<HAPRootStructure> out = new ArrayList<HAPRootStructure>();
		if(rootsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)rootsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPRootStructure root = HAPParserStructure.parseContextRootFromJson(eleDefJson);
				root.setName(eleName);
				out.add(root);
			}
		}
		else if(rootsObj instanceof JSONArray) {
			JSONArray elementsArray = (JSONArray)rootsObj;
			for(int i=0; i<elementsArray.length(); i++) {
				JSONObject eleDefJson = elementsArray.getJSONObject(i);
				HAPRootStructure root = HAPParserStructure.parseContextRootFromJson(eleDefJson);
				out.add(root);
			}
		}
		return out;
	}
	
	//parse context root
	public static HAPRootStructure parseContextRootFromJson(JSONObject eleDefJson){
		HAPRootStructure out = new HAPRootStructure();

		//info
		out.buildEntityInfoByJson(eleDefJson);

		//local id
		out.setLocalId((String)eleDefJson.opt(HAPRootStructure.LOCALID));
		
		//global id
		out.setGlobalId((String)eleDefJson.opt(HAPRootStructure.GLOBALID));
		
		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPRootStructure.DEFINITION);
		if(defJsonObj!=null)  out.setDefinition(parseContextDefinitionElement(defJsonObj));
		else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementStructureLeafData());
		}
		Object defaultJsonObj = eleDefJson.opt(HAPRootStructure.DEFAULT);
		out.setDefaultValue(defaultJsonObj);
		return out;
	}
	
	public static HAPElementStructure parseContextDefinitionElement(JSONObject eleDefJson) {
		HAPElementStructure contextRootDef = null;
		
		Object pathObj = eleDefJson.opt(HAPElementStructureLeafRelative.PATH);
		Object criteriaDef = eleDefJson.opt(HAPElementStructureLeafData.CRITERIA);
		Object valueJsonObj = eleDefJson.opt(HAPElementStructureLeafConstant.VALUE);
		JSONObject childrenJsonObj = eleDefJson.optJSONObject(HAPElementStructureNode.CHILD);
		String constantName = (String)eleDefJson.opt(HAPElementStructureLeafConstantReference.CONSTANT);
		
		if(pathObj!=null){
			//relative
			contextRootDef = new HAPElementStructureLeafRelative();
			HAPElementStructureLeafRelative relativeLeaf = (HAPElementStructureLeafRelative)contextRootDef;
			
			HAPInfoPathReference path = new HAPInfoPathReference();
			path.buildObject(eleDefJson.get(HAPElementStructureLeafRelative.PATH), HAPSerializationFormat.JSON);
			relativeLeaf.setPath(path);
			
			Object resolvedPathObj = eleDefJson.opt(HAPElementStructureLeafRelative.RESOLVEDPATH);
			if(resolvedPathObj!=null) {
				if(resolvedPathObj instanceof String) {
					HAPComplexPath resolvedPath = new HAPComplexPath((String)resolvedPathObj);
					relativeLeaf.setResolvedIdPath(resolvedPath);
				}
				else {
					HAPComplexPath resolvedPath = new HAPComplexPath();
					resolvedPath.buildObject(resolvedPathObj, HAPSerializationFormat.JSON);
					relativeLeaf.setResolvedIdPath(resolvedPath);
				}
			}
			
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPElementStructureLeafRelative.DEFINITION);
			if(definitionJsonObj!=null) 	relativeLeaf.setDefinition(parseContextDefinitionElement(definitionJsonObj));
			
			JSONObject solidNodeRefJsonObj = eleDefJson.optJSONObject(HAPElementStructureLeafRelative.SOLIDNODEREF);
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
			contextRootDef = new HAPElementStructureLeafData(dataInfo);   
		}
		else if(childrenJsonObj!=null) {
			//node
			contextRootDef = new HAPElementStructureNode();
			for(Object key : childrenJsonObj.keySet()) {
				((HAPElementStructureNode)contextRootDef).addChild((String)key, parseContextDefinitionElement(childrenJsonObj.getJSONObject((String)key)));
			}
		}
		else if(valueJsonObj!=null){
			//constant
			contextRootDef = new HAPElementStructureLeafConstant();
			((HAPElementStructureLeafConstant)contextRootDef).setValue(valueJsonObj);
		}
		else if(constantName!=null) {
			//constant reference
			contextRootDef = new HAPElementStructureLeafConstantReference(constantName);
		}
		else {
			//value
			contextRootDef = new HAPElementStructureLeafValue();
		}
		return contextRootDef;
	}

}
