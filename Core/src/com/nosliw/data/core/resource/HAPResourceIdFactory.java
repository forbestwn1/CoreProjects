package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;

public class HAPResourceIdFactory {

	private static HAPResourceId newInstanceByType(String resourceType, String structure) {
		HAPResourceId out = null;
		if(structure.equals(HAPConstant.RESOURCEID_TYPE_SIMPLE)) {
			out = new HAPResourceIdSimple(resourceType);
		}
		else if(structure.equals(HAPConstant.RESOURCEID_TYPE_EMBEDED)) {
			out = new HAPResourceIdEmbeded(resourceType);
		}
		else if(structure.equals(HAPConstant.RESOURCEID_TYPE_DYNAMIC)) {
			out = new HAPResourceIdDynamic(resourceType);
		}
		return out;
	}
	
	public static HAPResourceId newInstance(String type, Object coreIdObj) {
		HAPResourceId out = null;
		if(coreIdObj instanceof String) {
			String[] segs = HAPResourceUtility.parseResourceCoreIdLiterate((String)coreIdObj);
			out = HAPResourceIdFactory.newInstanceByType(type, segs[0]);
			out.buildCoreIdByLiterate(segs[1]);
		}
		else if(coreIdObj instanceof JSONObject) {
			JSONObject coreIdJson = (JSONObject)coreIdObj;
			String structure = coreIdJson.optString(HAPResourceId.STRUCUTRE);
			if(HAPBasicUtility.isStringEmpty(structure))  structure = HAPResourceUtility.getDefaultResourceStructure();
			out = HAPResourceIdFactory.newInstanceByType(type, structure);
			out.buildCoreIdByJSON(coreIdJson);
		}
		return out;
	}
	
	public static HAPResourceId newInstance(Object content) {
		HAPResourceId out = null;
		if(content instanceof String) {
			String literate = (String)content;
			String[] idSegs = HAPResourceUtility.parseResourceIdLiterate(literate);
			if(idSegs.length==1)   out = newInstance(null, idSegs[0]);
			else if(idSegs.length==2)   out = newInstance(idSegs[0], idSegs[1]);
		}
		else if(content instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)content;
			out = newInstance(jsonObj.getString(HAPResourceId.TYPE), jsonObj.get(HAPResourceId.ID));
		}
		return out;
	}
	
	public static HAPResourceId newInstance(HAPResourceId resourceId, List<HAPResourceDependency> supplement){
		return newInstance(resourceId, HAPResourceIdSupplement.newInstance(supplement));
	}

	public static HAPResourceId newInstance(HAPResourceId resourceId, HAPResourceIdSupplement supplement){
		HAPResourceId out = resourceId.clone();
		out.setSupplement(supplement);
		return out;
	}
	
	public static List<HAPResourceId> newInstanceList(JSONArray resourceJsonArray){
		List<HAPResourceId> resourceIds = new ArrayList<>();
		for(int i=0; i<resourceJsonArray.length(); i++) {
			resourceIds.add(newInstance(resourceJsonArray.getJSONObject(i)));
		}
		return resourceIds;
	}


//	public static HAPResourceId newInstance(String type, Object id) {    
//		HAPResourceIdSimple out = new HAPResourceIdSimple();
//		if(id instanceof String)	out.init(type, (String)id, null);
//		else if(id instanceof JSONObject) {
//			out.m_type = type;
//			out.buildObjectByJson(id);
//		}
//		return out;
//	}
	
}
