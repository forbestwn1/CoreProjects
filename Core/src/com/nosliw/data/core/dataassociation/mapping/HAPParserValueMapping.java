package com.nosliw.data.core.dataassociation.mapping;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.structure.HAPParserStructure;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;

public class HAPParserValueMapping {

	public static List<HAPItemValueMapping<HAPReferenceElementInValueContext>> parses(Object itemsObj){
		List<HAPItemValueMapping<HAPReferenceElementInValueContext>> out = new ArrayList<>();
		if(itemsObj instanceof JSONObject) {
			JSONObject elementsJson = (JSONObject)itemsObj;
			Iterator<String> it = elementsJson.keys();
			while(it.hasNext()){
				String eleKey = it.next();
				JSONObject eleDefJson = elementsJson.optJSONObject(eleKey);
				HAPItemValueMapping<HAPReferenceElementInValueContext> item = parseValueMappingItemFromJson(eleDefJson);
				if(item!=null) {
					HAPReferenceElementInValueContext target = new HAPReferenceElementInValueContext();
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
				HAPItemValueMapping<HAPReferenceElementInValueContext> item = parseValueMappingItemFromJson(eleDefJson);
				if(item!=null)  out.add(item);
			}
		}
		return out;
	}
	
	//parse context root
	public static HAPItemValueMapping<HAPReferenceElementInValueContext> parseValueMappingItemFromJson(JSONObject eleDefJson){
		HAPItemValueMapping<HAPReferenceElementInValueContext> out = new HAPItemValueMapping<HAPReferenceElementInValueContext>();

		//info
		out.buildEntityInfoByJson(eleDefJson);
		if(!HAPUtilityEntityInfo.isEnabled(out))   return null;

		//target
		Object targetObj = eleDefJson.opt(HAPItemValueMapping.TARGET);
		if(targetObj!=null) {
			HAPReferenceElementInValueContext target = new HAPReferenceElementInValueContext();
			target.buildObject(targetObj, HAPSerializationFormat.JSON);
		}
		
		//definition
		JSONObject defJsonObj = eleDefJson.getJSONObject(HAPItemValueMapping.DEFINITION);
		out.setDefinition(HAPParserStructure.parseStructureElement(defJsonObj));
		return out;
	}
}
