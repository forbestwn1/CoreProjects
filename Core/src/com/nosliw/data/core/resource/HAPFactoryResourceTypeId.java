package com.nosliw.data.core.resource;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPFactoryResourceTypeId {

	public static HAPIdResourceType newInstance(String resourceType, String version) {
		HAPIdResourceType out = new HAPIdResourceType(resourceType, version);
		out = normalizeResouceTypeId(out);
		return out;
	}
	
	public static HAPIdResourceType newInstance(Object obj) {
		HAPIdResourceType out = tryParseResourceTypeId(obj);
		out = normalizeResouceTypeId(out);
		return out;
	}

	public static HAPIdResourceType newInstance(Object obj, HAPIdResourceType fallbackResourceTypeId) {
		HAPIdResourceType out = tryParseResourceTypeId(obj);
		if(fallbackResourceTypeId!=null) {
			if(out.getResourceType()==null) {
				out.setResourceType(fallbackResourceTypeId.getResourceType());
			}
			if(out.getVersion()==null) {
				out.setVersion(fallbackResourceTypeId.getVersion());
			}
		}
		out = normalizeResouceTypeId(out);
		return out;
	}

	private static HAPIdResourceType tryParseResourceTypeId(Object obj) {
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
	
	private static HAPIdResourceType normalizeResouceTypeId(HAPIdResourceType resourceTypeId) {
		if(resourceTypeId.getVersion()==null) {
			resourceTypeId.setVersion(HAPConstantShared.VERSION_DEFAULT);
		}
		return resourceTypeId;
	}
	
}
