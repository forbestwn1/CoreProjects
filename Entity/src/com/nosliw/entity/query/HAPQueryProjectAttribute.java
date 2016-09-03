package com.nosliw.entity.query;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.common.utils.HAPSegmentParser;
import com.nosliw.entity.utils.HAPAttributeConstant;

/*
 * store information related with project attribute in query
 * 		entityName
 * 		attribute
 */
public class HAPQueryProjectAttribute implements HAPStringable{
	public String entityName;
	public String attribute;
	
	public HAPQueryProjectAttribute(String attPath){
		HAPSegmentParser parser = new HAPSegmentParser(attPath);
		this.entityName = parser.next();
		this.attribute = parser.getRestPath();
	}
	
	public HAPQueryProjectAttribute(String entityName, String attribute){
		this.entityName = entityName;
		this.attribute = attribute;
	}
	
	public String toString(){
		return HAPNamingConversionUtility.cascadePath(entityName, attribute);
	}

	@Override
	public String toStringValue(String format) {
		
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(HAPAttributeConstant.QUERYPROJECTATTRIBUTE_ENTITYNAME, this.entityName);
		jsonMap.put(HAPAttributeConstant.QUERYPROJECTATTRIBUTE_ATTRIBUTE, this.attribute);
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}
	
	static public HAPQueryProjectAttribute parse(JSONObject jsonObject){
		String entityName = jsonObject.optString(HAPAttributeConstant.QUERYPROJECTATTRIBUTE_ENTITYNAME);
		String attribute = jsonObject.optString(HAPAttributeConstant.QUERYPROJECTATTRIBUTE_ATTRIBUTE);
		return new HAPQueryProjectAttribute(entityName, attribute);
	}
}
