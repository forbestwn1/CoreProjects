package com.nosliw.core.application.common.dataassociation.definition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.structure.HAPUtilityParserElement;
import com.nosliw.core.application.valueport.HAPReferenceElement;
import com.nosliw.core.application.valueport.HAPReferenceRootElement;

public class HAPDefinitionParserMapping {

	public static List<HAPDefinitionMappingItemValue> parses(Object itemsObj){
		List<HAPDefinitionMappingItemValue> out = new ArrayList<>();
		if(itemsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)itemsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleKey = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleKey);
				HAPDefinitionMappingItemValue item = parseValueMappingItemFromJson(eleDefJson);
				if(item!=null) {
					HAPReferenceElement target = new HAPReferenceElement();
					target.buildObject(eleKey, HAPSerializationFormat.JSON);
					item.setTarget(target);
					out.add(item);
				}
			}
		}
		else if(itemsObj instanceof JSONArray) {
			JSONArray elementsArray = (JSONArray)itemsObj;
			for(int i=0; i<elementsArray.length(); i++) {
				JSONObject eleDefJson = elementsArray.getJSONObject(i);
				HAPDefinitionMappingItemValue item = parseValueMappingItemFromJson(eleDefJson);
				if(item!=null) {
					out.add(item);
				}
			}
		}
		return out;
	}
	
	//parse context root
	public static HAPDefinitionMappingItemValue parseValueMappingItemFromJson(JSONObject eleDefJson){
		HAPDefinitionMappingItemValue out = new HAPDefinitionMappingItemValue();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out)) {
			return null;
		}

		//target
		Object targetObj = eleDefJson.opt(HAPDefinitionMappingItemValue.TARGET);
		if(targetObj!=null) {
			HAPReferenceRootElement target = new HAPReferenceRootElement();
			target.buildObject(targetObj, HAPSerializationFormat.JSON);
			out.setTarget(target);
		}
		
		//definition
		JSONObject defJsonObj = eleDefJson.getJSONObject(HAPDefinitionMappingItemValue.DEFINITION);
		out.setDefinition(HAPUtilityParserElement.parseStructureElement(defJsonObj));
		return out;
	}
}
