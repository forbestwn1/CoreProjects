package com.nosliw.core.application.common.structure;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.valueport.HAPReferenceElement;

public class HAPParserStructure {

	public static HAPElementStructure parseStructureElement(JSONObject eleDefJson) {
		HAPElementStructure out = null;
		
		Object defRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForDefinition.REFERENCE);
		Object linkRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForValue.LINK);
		Object mappingRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForMapping.MAPPING);
		Object provideObj = eleDefJson.opt(HAPElementStructureLeafProvide.PROVIDE);
		Object criteriaDef = eleDefJson.opt(HAPElementStructureLeafData.CRITERIA);
		Object valueJsonObj = eleDefJson.opt(HAPElementStructureLeafConstant.VALUE);
		JSONObject childrenJsonObj = eleDefJson.optJSONObject(HAPElementStructureNode.CHILD);
		String constantName = (String)eleDefJson.opt(HAPElementStructureLeafConstantReference.CONSTANT);
		Object runtimeObj = eleDefJson.opt(HAPElementStructureLeafRuntime.RUNTIME);
		
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
			if(definitionJsonObj!=null) {
				relativeEle.setDefinition(parseStructureElement(definitionJsonObj));
			}
		}
		else if(mappingRefObj!=null) {
			//relative for mapping
			out = new HAPElementStructureLeafRelativeForMapping();
			parseRelativeElement((HAPElementStructureLeafRelativeForMapping)out, mappingRefObj, eleDefJson);
		}
		else if(provideObj!=null) {
			HAPElementStructureLeafProvide provideElement = new HAPElementStructureLeafProvide();
			if(provideObj instanceof String) {
				provideElement.setName((String)provideObj);
			}
			else if(provideObj instanceof JSONObject) {
				JSONObject provideEleJsonObj = (JSONObject)provideObj;
				provideElement.setName(provideEleJsonObj.getString(HAPElementStructureLeafProvide.NAME));
				JSONObject defJsonObj = provideEleJsonObj.optJSONObject(HAPElementStructureLeafProvide.DEFINITION);
				if(defJsonObj!=null) {
					provideElement.setDefinition(HAPParserStructure.parseStructureElement(defJsonObj));
				}
			}
			out = provideElement;
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
		else if(runtimeObj!=null) {
			out = new HAPElementStructureLeafRuntime();
		}
		else {
			//value
			out = new HAPElementStructureLeafValue();
		}
		return out;
	}

	private static void parseRelativeElement(HAPElementStructureLeafRelative relativeEle, Object refObj, JSONObject eleDefJson) {
		
		HAPReferenceElement path = new HAPReferenceElement();
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
