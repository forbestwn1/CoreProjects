package com.nosliw.data.core.structure;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

public class HAPParserStructure {

	public static List<HAPRootStructure> parseStructureRoots(Object rootsObj){
		List<HAPRootStructure> out = new ArrayList<HAPRootStructure>();
		if(rootsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)rootsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleName = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleName);
				HAPRootStructure root = HAPParserStructure.parseStructureRootFromJson(eleDefJson);
				if(root!=null) {
					root.setName(eleName);
					out.add(root);
				}
			}
		}
		else if(rootsObj instanceof JSONArray) {
			JSONArray elementsArray = (JSONArray)rootsObj;
			for(int i=0; i<elementsArray.length(); i++) {
				JSONObject eleDefJson = elementsArray.getJSONObject(i);
				HAPRootStructure root = HAPParserStructure.parseStructureRootFromJson(eleDefJson);
				out.add(root);
			}
		}
		return out;
	}
	
	//parse context root
	public static HAPRootStructure parseStructureRootFromJson(JSONObject eleDefJson){
		HAPRootStructure out = new HAPRootStructure();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out))   return null;

		//local id
		out.setLocalId((String)eleDefJson.opt(HAPRootStructure.LOCALID));
		
		//global id
		out.setGlobalId((String)eleDefJson.opt(HAPRootStructure.GLOBALID));
		
		//definition
		JSONObject defJsonObj = eleDefJson.optJSONObject(HAPRootStructure.DEFINITION);
		if(defJsonObj!=null)  out.setDefinition(parseStructureElement(defJsonObj));
		else{
			//if no definition, then treat it as data leaf
			out.setDefinition(new HAPElementStructureLeafData());
		}
		Object defaultJsonObj = eleDefJson.opt(HAPRootStructure.DEFAULT);
		out.setDefaultValue(defaultJsonObj);
		return out;
	}
	
	public static HAPElementStructure parseStructureElement(JSONObject eleDefJson) {
		HAPElementStructure out = null;
		
		Object defRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForDefinition.REFERENCE);
		Object linkRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForValue.LINK);
		Object criteriaDef = eleDefJson.opt(HAPElementStructureLeafData.CRITERIA);
		Object valueJsonObj = eleDefJson.opt(HAPElementStructureLeafConstant.VALUE);
		JSONObject childrenJsonObj = eleDefJson.optJSONObject(HAPElementStructureNode.CHILD);
		String constantName = (String)eleDefJson.opt(HAPElementStructureLeafConstantReference.CONSTANT);
		
		if(defRefObj!=null) {
			//relative for definition
			out = new HAPElementStructureLeafRelativeForDefinition();
			parseRelativeElement((HAPElementStructureLeafRelativeForDefinition)out, defRefObj, eleDefJson);
		}
		else if(linkRefObj!=null){
			//relative for value link
			HAPElementStructureLeafRelativeForValue relativeEle = new HAPElementStructureLeafRelativeForValue();
			out = relativeEle;
			parseRelativeElement(relativeEle, linkRefObj, eleDefJson);
			
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPElementStructureLeafRelativeForValue.DEFINITION);
			if(definitionJsonObj!=null) 	relativeEle.setDefinition(parseStructureElement(definitionJsonObj));
		}
		else if(criteriaDef!=null) {
			//data
			HAPVariableDataInfo dataInfo = new HAPVariableDataInfo();
			dataInfo.buildObject(criteriaDef, null);
			out = new HAPElementStructureLeafData(dataInfo);   
		}
		else if(childrenJsonObj!=null) {
			//node
			out = new HAPElementStructureNode();
			for(Object key : childrenJsonObj.keySet()) {
				((HAPElementStructureNode)out).addChild((String)key, parseStructureElement(childrenJsonObj.getJSONObject((String)key)));
			}
		}
		else if(valueJsonObj!=null){
			//constant
			out = new HAPElementStructureLeafConstant();
			((HAPElementStructureLeafConstant)out).setValue(valueJsonObj);
		}
		else if(constantName!=null) {
			//constant reference
			out = new HAPElementStructureLeafConstantReference(constantName);
		}
		else {
			//value
			out = new HAPElementStructureLeafValue();
		}
		return out;
	}

	private static void parseRelativeElement(HAPElementStructureLeafRelative relativeEle, Object refObj, JSONObject eleDefJson) {
		
		HAPReferenceElementInValueContext path = new HAPReferenceElementInValueContext();
		path.buildObject(refObj, HAPSerializationFormat.JSON);
		relativeEle.setReference(path);

		JSONObject resolvedInfoJsonObj = eleDefJson.optJSONObject(HAPElementStructureLeafRelative.RESOLVEDINFO);
		if(resolvedInfoJsonObj!=null) {
			HAPInfoRelativeResolve resolvedInfo = new HAPInfoRelativeResolve();
			resolvedInfo.buildObject(resolvedInfoJsonObj, HAPSerializationFormat.JSON);
			relativeEle.setResolvedInfo(resolvedInfo);
		}
		
		JSONObject solidNodeRefJsonObj = eleDefJson.optJSONObject(HAPElementStructureLeafRelative.SOLIDNODEREF);
		if(solidNodeRefJsonObj!=null) {
			HAPInfoPathToSolidRoot solidNodeRef = new HAPInfoPathToSolidRoot();
			solidNodeRef.buildObject(solidNodeRefJsonObj, HAPSerializationFormat.JSON);
			relativeEle.setSolidNodeReference(solidNodeRef);
		}
	}
}
