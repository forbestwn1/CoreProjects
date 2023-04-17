package com.nosliw.data.core.structure;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

public class HAPParserStructure {

	public static HAPElementStructure parseStructureElement(JSONObject eleDefJson) {
		HAPElementStructure out = null;
		
		Object defRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForDefinition.REFERENCE);
		Object linkRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForValue.LINK);
		Object mappingRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForMapping.MAPPING);
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
		else if(mappingRefObj!=null) {
			//relative for mapping
			out = new HAPElementStructureLeafRelativeForMapping();
			parseRelativeElement((HAPElementStructureLeafRelativeForMapping)out, mappingRefObj, eleDefJson);
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
