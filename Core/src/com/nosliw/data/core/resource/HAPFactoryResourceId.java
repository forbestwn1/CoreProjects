package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPFactoryResourceId {

	private static HAPResourceId newInstanceByType(String resourceType, String structure) {
		HAPResourceId out = null;
		if(structure==null)   structure = HAPUtilityResource.getDefaultResourceStructure();
		if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			out = new HAPResourceIdSimple(resourceType);
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			out = new HAPResourceIdEmbeded(resourceType);
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_DYNAMIC)) {
			out = new HAPResourceIdDynamic(resourceType);
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			out = new HAPResourceIdLocal(resourceType);
		}
		return out;
	}
	
	public static HAPResourceId newInstance(String type, Object obj) {
		Object coreIdObj = obj;
		String structure = null;
		if(obj instanceof JSONObject) {
			Object typeObj = ((JSONObject) obj).opt(HAPResourceId.TYPE);
			structure = (String)((JSONObject) obj).opt(HAPResourceId.STRUCUTRE);
			if(typeObj!=null) type = (String)typeObj;
			
			if(structure!=null || typeObj!=null) {
				coreIdObj = ((JSONObject) obj).opt(HAPResourceId.ID);
			}
		}
		
		HAPResourceId out = null;
		if(coreIdObj instanceof String) {
			if(structure!=null) {
				out = HAPFactoryResourceId.newInstanceByType(type, structure);
				out.buildCoreIdByLiterate((String)coreIdObj);
			}
			else {
				String[] segs = HAPUtilityResource.parseResourceCoreIdLiterate((String)coreIdObj);
				out = HAPFactoryResourceId.newInstanceByType(type, segs[0]);
				out.buildCoreIdByLiterate(segs[1]);
			}
		}
		else if(coreIdObj instanceof JSONObject) {
			JSONObject coreIdJson = (JSONObject)coreIdObj;
			out = HAPFactoryResourceId.newInstanceByType(type, structure);
			out.buildCoreIdByJSON(coreIdJson);
		}
		return out;
	}
	
	public static HAPResourceId newInstance(Object content) {
		HAPResourceId out = null;
		if(content instanceof String) {
			String literate = (String)content;
			String[] idSegs = HAPUtilityResource.parseResourceIdLiterate(literate);
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
