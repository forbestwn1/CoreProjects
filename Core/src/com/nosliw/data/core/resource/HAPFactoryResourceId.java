package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class HAPFactoryResourceId {

	public static HAPResourceId newInstance(String resourceType, Object obj) {
		HAPResourceId out = null;
		if(obj instanceof String)  out = HAPUtilityResourceId.buildResourceIdByLiterate(resourceType, (String)obj, false);
		else if(obj instanceof JSONObject)  out = newInstanceByJSONObect(resourceType, (JSONObject)obj);
		return out;
	}
	
	private static HAPResourceId newInstanceByJSONObect(String resourceType, JSONObject jsonObj) {
		Object coreIdObj = jsonObj;
		
		Object typeObj = jsonObj.opt(HAPResourceId.RESOURCETYPE);
		String structure = (String)jsonObj.opt(HAPResourceId.STRUCUTRE);
		if(typeObj!=null) resourceType = (String)typeObj;
		
		if(structure!=null || typeObj!=null) {
			coreIdObj = jsonObj.opt(HAPResourceId.ID);
		}
		
		HAPResourceId out = null;
		if(coreIdObj instanceof String) {
			if(structure!=null) {
				out = HAPUtilityResourceId.newInstanceByType(resourceType, structure);
				out.buildCoreIdByLiterate((String)coreIdObj);
			}
			else {
				out = HAPUtilityResourceId.buildResourceIdByLiterate(resourceType, (String)coreIdObj, false);
				
//				String[] segs = HAPUtilityResourceId.parseResourceCoreIdLiterate((String)coreIdObj);
//				out = HAPUtilityResourceId.newInstanceByType(resourceType, segs[0]);
//				out.buildCoreIdByLiterate(segs[1]);
			}
		}
		else if(coreIdObj instanceof JSONObject) {
			JSONObject coreIdJson = (JSONObject)coreIdObj;
			out = HAPUtilityResourceId.newInstanceByType(resourceType, structure);
			out.buildCoreIdByJSON(coreIdJson);
		}
		return out;
		
	}
	
	public static HAPResourceId newInstance(Object content) {
		HAPResourceId out = null;
		if(content instanceof String) {
			String literate = (String)content;
			String[] idSegs = HAPUtilityResourceId.parseResourceIdLiterate(literate);
			if(idSegs.length==1)   out = newInstance(null, idSegs[0]);
			else if(idSegs.length==2)   out = newInstance(idSegs[0], idSegs[1]);
		}
		else if(content instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)content;
			out = newInstance(jsonObj.getString(HAPResourceId.RESOURCETYPE), jsonObj.get(HAPResourceId.ID));
		}
		return out;
	}
	
	public static HAPResourceId newInstance(HAPResourceId resourceId, List<HAPResourceDependency> supplement){
		return newInstance(resourceId, HAPSupplementResourceId.newInstance(supplement));
	}

	public static HAPResourceId newInstance(HAPResourceId resourceId, HAPSupplementResourceId supplement){
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
