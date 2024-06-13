package com.nosliw.data.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPResourceHelper {
	private static HAPResourceHelper m_instance;

	private Map<String, Class> m_typeToResourceId;
	private Map<Class, Class> m_idToResourceId;
	
	public static HAPResourceHelper getInstance(){
		if(m_instance==null){
			m_instance = new HAPResourceHelper();
		}
		return m_instance;
	}
	
	private HAPResourceHelper(){	
		this.m_typeToResourceId = new LinkedHashMap<String, Class>();
		this.m_idToResourceId = new LinkedHashMap<Class, Class>();
	}
	
	public void registerResourceId(String resourceType, Class resourceIdClass, Class dataIdClass){
		this.m_typeToResourceId.put(resourceType, resourceIdClass);
		this.m_idToResourceId.put(dataIdClass, resourceIdClass);
	}

	public HAPResourceIdSimple buildResourceIdObject(JSONObject jsonObj){
		HAPResourceIdSimple out = (HAPResourceIdSimple)HAPFactoryResourceId.newInstance(jsonObj);
		try {
			Class resourceIdClass = this.m_typeToResourceId.get(out.getResourceTypeId().getResourceType());
			out = (HAPResourceIdSimple)resourceIdClass.getConstructor(HAPResourceIdSimple.class).newInstance(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public HAPResourceIdSimple buildResourceIdObject(String literate){
		HAPResourceIdSimple out = (HAPResourceIdSimple)HAPFactoryResourceId.newInstance(literate);
		try {
			Class resourceIdClass = this.m_typeToResourceId.get(out.getResourceTypeId().getResourceType());
			out = (HAPResourceIdSimple)resourceIdClass.getConstructor(HAPResourceIdSimple.class).newInstance(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}

	public String buildResourceIdLiterate(HAPResourceIdSimple resourceId){
		return resourceId.toStringValue(HAPSerializationFormat.LITERATE);
	}
	
	public HAPResourceIdSimple buildResourceIdFromIdData(Object resourceIdData){
		HAPResourceIdSimple out = null;
		Class resourceIdClass = this.m_idToResourceId.get(resourceIdData.getClass());
		try {
			out = (HAPResourceIdSimple)resourceIdClass.getConstructor(resourceIdData.getClass()).newInstance(resourceIdData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
}
