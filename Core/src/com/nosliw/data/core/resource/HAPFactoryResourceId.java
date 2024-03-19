package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class HAPFactoryResourceId {

	//newInstance when not sure obj including resource type or not.
	public static HAPResourceId tryNewInstance(String resourceType, String version, Object obj, boolean resourceTypeRestrict) {
		HAPResourceId out = null;
		out = newInstance(obj);
		if(out==null) {
			out = newInstance(resourceType, version, obj);
		}
		if(resourceTypeRestrict==true&&!out.getResourceType().equals(resourceType)) {
			throw new RuntimeException();
		}
		return out;
	}
	
	public static HAPResourceId tryNewInstance(String resourceType, String version, Object obj) {
		return tryNewInstance(resourceType, version, obj, true);
	}
	
	public static HAPResourceId newInstance(String resourceType, String version, Object obj) {
		HAPResourceId out = null;
		if(obj instanceof String) {
			out = HAPUtilityResourceId.buildResourceIdByLiterate(resourceType, version, (String)obj, false);
		} else if(obj instanceof JSONObject) {
			out = newInstanceByJSONObect(resourceType, version, (JSONObject)obj);
		}
		return out;
	}
	
	private static HAPResourceId newInstanceByJSONObect(String resourceType, String version, JSONObject jsonObj) {
		Object coreIdObj = jsonObj;
		
		Object typeObj = jsonObj.opt(HAPResourceId.RESOURCETYPE);
		String structure = (String)jsonObj.opt(HAPResourceId.STRUCUTRE);
		if(typeObj!=null) {
			resourceType = (String)typeObj;
		}
		
		if(structure!=null || typeObj!=null) {
			coreIdObj = jsonObj.opt(HAPResourceId.ID);
		}
		
		HAPResourceId out = null;
		if(coreIdObj instanceof String) {
			if(structure!=null) {
				out = HAPUtilityResourceId.newInstanceByType(resourceType, version, structure);
				out.buildCoreIdByLiterate((String)coreIdObj);
			}
			else {
				out = HAPUtilityResourceId.buildResourceIdByLiterate(resourceType, version, (String)coreIdObj, false);
				
//				String[] segs = HAPUtilityResourceId.parseResourceCoreIdLiterate((String)coreIdObj);
//				out = HAPUtilityResourceId.newInstanceByType(resourceType, segs[0]);
//				out.buildCoreIdByLiterate(segs[1]);
			}
		}
		else if(coreIdObj instanceof JSONObject) {
			JSONObject coreIdJson = (JSONObject)coreIdObj;
			out = HAPUtilityResourceId.newInstanceByType(resourceType, version, structure);
			out.buildCoreIdByJSON(coreIdJson);
		}
		return out;
		
	}
	
	public static HAPResourceId newInstance(Object content) {
		HAPResourceId out = null;
		if(content instanceof String) {
			String literate = (String)content;
			String[] idSegs = HAPUtilityResourceId.parseResourceIdLiterate(literate);
			if(idSegs.length==1) {
				out = null;
			} else if(idSegs.length==3) {
				out = newInstance(idSegs[0], idSegs[1], idSegs[2]);
			}
		}
		else if(content instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)content;
			Object resourceType = jsonObj.opt(HAPResourceId.RESOURCETYPE);
			Object resourceVersion = jsonObj.opt(HAPResourceId.VERSION);
			if(resourceType==null) {
				out = null;
			}
			out = newInstance((String)resourceType, (String)resourceVersion, jsonObj.get(HAPResourceId.ID));
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
			resourceIds.add(newInstance(resourceJsonArray.get(i)));
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
