package com.nosliw.data.core.structure;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;

public class HAPParserStructure {

	
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
			String parent = (String)eleDefJson.opt(HAPElementStructureLeafRelative.PARENT);
			relativeLeaf.setParent(parent);
			if(pathObj instanceof String)	relativeLeaf.setReferencePath((String)pathObj);
			else if(pathObj instanceof JSONObject){
				HAPReferenceElement contextPath = new HAPReferenceElement();
				contextPath.buildObject(pathObj, HAPSerializationFormat.JSON);
				relativeLeaf.setReferencePath(contextPath.toStringValue(HAPSerializationFormat.LITERATE));
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
