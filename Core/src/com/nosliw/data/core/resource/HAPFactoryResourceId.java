package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPFactoryResourceId {

	//newInstance when not sure obj including resource type or not.
	public static HAPResourceId tryNewInstance(String resourceType, String version, Object obj, boolean resourceTypeRestrict) {
		HAPResourceId out = null;
		out = newInstance(obj);
		if(out==null) {
			out = newInstance(resourceType, version, obj);
		}
		
		if(resourceTypeRestrict==true&&
			!HAPUtilityBasic.isEquals(out.getResourceTypeId().getResourceType(), resourceType)&&
			!HAPUtilityBasic.isEquals(out.getResourceTypeId().getVersion(), version)) {
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
			
			String seperator = HAPConstantShared.SEPERATOR_LEVEL2;
			
			String resourceType = null;
			String version = null; 
			String idLiterate = null;

			int index = literate.indexOf(seperator);
			resourceType = literate.substring(0, index);
			literate = literate.substring(index+seperator.length());
			
			index = literate.indexOf(seperator);
			version = literate.substring(0, index);
			idLiterate = literate.substring(index+seperator.length());

			out = newInstance(resourceType, version, idLiterate);
		}
		else if(content instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)content;
			HAPIdResourceType resourceTypeId = null;
			Object resourceTypeIdObj = jsonObj.opt(HAPResourceId.RESOURCETYPEID);
			if(resourceTypeIdObj==null) {
				resourceTypeId = parseResourceTypeId(jsonObj);
			} else {
				resourceTypeId = parseResourceTypeId(resourceTypeIdObj);
			}
			
			out = newInstance(resourceTypeId.getResourceType(), resourceTypeId.getVersion(), jsonObj.get(HAPResourceId.ID));
		}
		return out;
	}
	
	public static HAPIdResourceType parseResourceTypeId(Object obj) {
		HAPIdResourceType out = null;
		if(obj instanceof String) {
			out = new HAPIdResourceType((String)obj);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPIdResourceType();
			out.buildObject(obj, HAPSerializationFormat.JSON);
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
