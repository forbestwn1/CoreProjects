package com.nosliw.data.core.resource;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
			out = buildResourceIdByLiterate(HAPFactoryResourceTypeId.newInstance(resourceType, version), (String)obj, false);
		} else if(obj instanceof JSONObject) {
			out = newInstanceByJSONObect(resourceType, version, (JSONObject)obj);
		}
		return out;
	}
	
	private static HAPResourceId newInstanceByJSONObect(String resourceType, String version, JSONObject jsonObj) {
		Object coreIdObj = jsonObj;
		
		String structure = (String)jsonObj.opt(HAPResourceId.STRUCUTRE);
		
		Object typeObj = jsonObj.opt(HAPResourceId.RESOURCETYPEID);
		HAPIdResourceType resourceTypeId = null;
		if(typeObj!=null) {
			resourceTypeId = HAPFactoryResourceTypeId.newInstance(typeObj, HAPFactoryResourceTypeId.newInstance(resourceType, version));
		}
		else {
			resourceTypeId = HAPFactoryResourceTypeId.newInstance(resourceType, version);
		}
		
		if(structure!=null || typeObj!=null) {
			coreIdObj = jsonObj.opt(HAPResourceId.ID);
		}
		
		HAPResourceId out = null;
		if(coreIdObj instanceof String) {
			if(structure!=null) {
				out = newInstanceByType(resourceTypeId, structure);
				out.buildCoreIdByLiterate((String)coreIdObj);
			}
			else {
				out = buildResourceIdByLiterate(resourceTypeId, (String)coreIdObj, false);
			}
		}
		else if(coreIdObj instanceof JSONObject) {
			JSONObject coreIdJson = (JSONObject)coreIdObj;
			out = newInstanceByType(resourceTypeId, structure);
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
			if(index==-1) {
				return null;
			}
			resourceType = literate.substring(0, index);
			literate = literate.substring(index+seperator.length());
			
			index = literate.indexOf(seperator);
			if(index==-1) {
				return null;
			}
			version = literate.substring(0, index);
			idLiterate = literate.substring(index+seperator.length());

			out = newInstance(resourceType, version, idLiterate);
		}
		else if(content instanceof JSONObject) {
			JSONObject jsonObj = (JSONObject)content;
			HAPIdResourceType resourceTypeId = null;
			Object resourceTypeIdObj = jsonObj.opt(HAPResourceId.RESOURCETYPEID);
			if(resourceTypeIdObj==null) {
				resourceTypeId = HAPFactoryResourceTypeId.newInstance(jsonObj);
			} else {
				resourceTypeId = HAPFactoryResourceTypeId.newInstance(resourceTypeIdObj);
			}
			
			out = newInstance(resourceTypeId.getResourceType(), resourceTypeId.getVersion(), jsonObj.get(HAPResourceId.ID));
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
	
	private static HAPResourceId newInstanceByType(HAPIdResourceType resourceTypeId, String structure) {
		HAPResourceId out = null;
		if(structure==null) {
			structure = getDefaultResourceStructure();
		}
		if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			out = new HAPResourceIdSimple(resourceTypeId.getResourceType(), resourceTypeId.getVersion());
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			out = new HAPResourceIdEmbeded(resourceTypeId.getResourceType(), resourceTypeId.getVersion());
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_DYNAMIC)) {
			out = new HAPResourceIdDynamic(resourceTypeId.getResourceType());
		}
		else if(structure.equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) {
			out = new HAPResourceIdLocal(resourceTypeId.getResourceType());
		}
		return out;
	}
	
	private static String getDefaultResourceStructure() {    return HAPConstantShared.RESOURCEID_TYPE_SIMPLE;     }

	public static HAPResourceId buildResourceIdByLiterate(HAPIdResourceType resourceTypeId, String literate, boolean strict) {
		String structure = null;
		String coreIdLiterate = literate;
		
		if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED)) {
			//embeded resource id
			structure = HAPConstantShared.RESOURCEID_TYPE_EMBEDED;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_EMBEDED.length());
		}
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL)){
			//local resource id
			structure = HAPConstantShared.RESOURCEID_TYPE_LOCAL;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_LOCAL.length());
		}
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC)){
			//local resource id
			structure = HAPConstantShared.RESOURCEID_TYPE_DYNAMIC;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_DYNAMIC.length());
		}
		else if(literate.startsWith(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE)){
			//simple
			structure = HAPConstantShared.RESOURCEID_TYPE_SIMPLE;
			coreIdLiterate = literate.substring(HAPConstantShared.RESOURCEID_LITERATE_STARTER_SIMPLE.length());
		}
		else if(!strict) {
			//if no sign, then by default simple
			structure = HAPConstantShared.RESOURCEID_TYPE_SIMPLE;
			coreIdLiterate = literate;
		}

		if(structure==null) {
			return null;
		}
		
		HAPResourceId out = newInstanceByType(resourceTypeId, structure);
		out.buildCoreIdByLiterate(coreIdLiterate);
		return out;
	}
	

}
