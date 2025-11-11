package com.nosliw.core.application.common.structure;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionWritable;
import com.nosliw.core.application.common.datadefinition.HAPParserDataDefinition;
import com.nosliw.core.application.common.structure.reference.HAPInfoRelativeResolve;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;
import com.nosliw.core.application.valueport.HAPReferenceElement;
import com.nosliw.core.xxx.application.common.structure.HAPElementStructureLeafConstantReference;
import com.nosliw.core.xxx.application.common.structure.HAPElementStructureLeafProvide;
import com.nosliw.core.xxx.application.common.structure.HAPElementStructureLeafRuntime;

public class HAPUtilityParserElement {

	public static HAPElementStructure parseStructureElement(JSONObject eleDefJson, HAPManagerDataRule dataRuleMan) {
		HAPElementStructure out = null;
		
		Object defRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForDefinition.REFERENCE);
		Object linkRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForValue.LINK);
		Object mappingRefObj = eleDefJson.opt(HAPElementStructureLeafRelativeForMapping.MAPPING);
		Object provideObj = eleDefJson.opt(HAPElementStructureLeafProvide.PROVIDE);
		Object criteriaDef = eleDefJson.opt(HAPElementStructureLeafData.DATA);
		Object valueJsonObj = eleDefJson.opt(HAPElementStructureLeafConstant.VALUE);
		String constantName = (String)eleDefJson.opt(HAPElementStructureLeafConstantReference.CONSTANT);
		Object runtimeObj = eleDefJson.opt(HAPElementStructureLeafRuntime.RUNTIME);
		
		if(defRefObj!=null) {
			//relative for definition
			out = new HAPElementStructureLeafRelativeForDefinition();
			parseRelativeElement((HAPElementStructureLeafRelativeForDefinition)out, defRefObj, eleDefJson, dataRuleMan);
		}
		else if(linkRefObj!=null){
			//relative for value link
			HAPElementStructureLeafRelativeForValue relativeEle = new HAPElementStructureLeafRelativeForValue();
			out = relativeEle;
			parseRelativeElement(relativeEle, linkRefObj, eleDefJson, dataRuleMan);
			
			JSONObject definitionJsonObj = eleDefJson.optJSONObject(HAPElementStructureLeafRelativeForValue.DEFINITION);
			if(definitionJsonObj!=null) {
				relativeEle.setDefinition((HAPElementStructureLeafData)parseStructureElement(definitionJsonObj, dataRuleMan));
			}
		}
		else if(mappingRefObj!=null) {
			//relative for mapping
			out = new HAPElementStructureLeafRelativeForMapping();
			parseRelativeElement((HAPElementStructureLeafRelativeForMapping)out, mappingRefObj, eleDefJson, dataRuleMan);
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
					provideElement.setDefinition(HAPUtilityParserElement.parseStructureElement(defJsonObj, dataRuleMan));
				}
			}
			out = provideElement;
		}
		else if(criteriaDef!=null) {
			//data
			HAPDataDefinitionWritable dataDef = HAPParserDataDefinition.parseDataDefinitionWritable(criteriaDef, dataRuleMan); 
			out = new HAPElementStructureLeafData(dataDef);   
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

	private static void parseRelativeElement(HAPElementStructureLeafRelative relativeEle, Object refObj, JSONObject eleDefJson, HAPManagerDataRule dataRuleMan) {
		
		HAPReferenceElement path = new HAPReferenceElement();
		path.buildObject(refObj, HAPSerializationFormat.JSON);
		relativeEle.setReference(path);

		JSONObject resolvedInfoJsonObj = eleDefJson.optJSONObject(HAPElementStructureLeafRelative.RESOLVEDINFO);
		if(resolvedInfoJsonObj!=null) {
			HAPInfoRelativeResolve resolvedInfo = HAPInfoRelativeResolve.parse(resolvedInfoJsonObj, dataRuleMan);
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
