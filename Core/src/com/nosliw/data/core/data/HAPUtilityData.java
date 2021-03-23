package com.nosliw.data.core.data;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPUtilityData {

	public static JSONObject createJSONData(String dataTypeId, Object value) throws JSONException{
		JSONObject out = new JSONObject();
		out.put(HAPData.VALUE, value);
		out.put(HAPData.DATATYPEID, dataTypeId);
		return out;
	}

	public static HAPRelationship cascadeRelationship(HAPRelationship r1, HAPRelationship r2) {
		return new HAPRelationshipImp(r1.getSource(), r2.getTarget(), cascadeRelationshipPath(r1.getPath(), r2.getPath()));
	}

	public static HAPRelationshipPath cascadeRelationshipPath(HAPRelationshipPath p1, HAPRelationshipPath p2) {
		HAPRelationshipPath out = new HAPRelationshipPath();
		
		List<HAPRelationshipPathSegment> segs1 = p1.getSegments();
		for(int i=0; i<segs1.size()-1; i++) 	out.addSegment(segs1.get(i));
		
		for(HAPRelationshipPathSegment seg2 : p2.getSegments())  out.addSegment(seg2);
		
		return out;
	}

	public static HAPDataWrapper buildDataWrapperFromObject(Object obj){
		if(obj==null)   return null;
		HAPDataWrapper out = null;
		if(obj instanceof String){
			out = buildDataWrapper((String)obj);
		}
		else if(obj instanceof JSONObject){
			out = buildDataWrapperFromJson((JSONObject)obj);
		}
		else if(obj instanceof HAPDataWrapper) {
			out = (HAPDataWrapper)obj;
		}
		return out;
	}
	
	
	public static HAPDataWrapper buildDataWrapper(String strValue){
		HAPDataWrapper wrapper = new HAPDataWrapper();
		if(wrapper.buildObjectByLiterate(strValue))  return wrapper;
		return null;
	}

	public static HAPDataWrapper buildDataWrapperFromJson(JSONObject jsonObj){
		if(jsonObj==null)   return null;
		HAPDataWrapper wrapper = new HAPDataWrapper();
		boolean result = wrapper.buildObjectByJson(jsonObj);
		if(result)   return wrapper;
		else return null;
	}

	public static Map<String, HAPData> buildDataWrapperMap(Object obj){
		JSONObject jsonObj = null;
		if(obj instanceof String) {
			if(HAPBasicUtility.isStringNotEmpty((String)obj)) {
				jsonObj = new JSONObject((String)obj);
			}
		}
		else if(obj instanceof JSONObject){
			jsonObj = (JSONObject)obj;
		}
		return buildDataWrapperMapFromJson(jsonObj);
	}
	
	public static Map<String, HAPData> buildDataWrapperMapFromJson(JSONObject jsonObj){
		Map<String, HAPData> constants = new LinkedHashMap<String, HAPData>();
		if(jsonObj!=null) {
			Iterator<String> it2 = jsonObj.keys();
			while(it2.hasNext()){
				String constantName = it2.next();
				JSONObject constantJson = jsonObj.getJSONObject(constantName);
				constants.put(constantName, HAPUtilityData.buildDataWrapperFromJson(constantJson));
			}
		}
		return constants;
	}
	
	public static boolean isNormalDataOpration(HAPOperation operation){
		String type = operation.getType();
		return type==null || HAPConstantShared.DATAOPERATION_TYPE_NORMAL.equals(type);
	}
	
}
