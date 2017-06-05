package com.nosliw.data.core.runtime;

import java.util.LinkedHashMap;
import java.util.Map;

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
	
	public HAPResourceId buildResourceIdObject(String literate){
		HAPResourceId out = new HAPResourceId(literate);
		try {
			Class resourceIdClass = this.m_typeToResourceId.get(out.getType());
			
			if(resourceIdClass==null){
				int kkkk = 5555;
				kkkk++;
			}
			
			out = (HAPResourceId)resourceIdClass.getConstructor(HAPResourceId.class).newInstance(out);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
	public String buildResourceIdLiterate(HAPResourceId resourceId){
		return resourceId.toStringValue(HAPSerializationFormat.LITERATE);
	}
	
	public HAPResourceId buildResourceIdFromIdData(Object resourceIdData){
		HAPResourceId out = null;
		Class resourceIdClass = this.m_idToResourceId.get(resourceIdData.getClass());
		try {
			out = (HAPResourceId)resourceIdClass.getConstructor(resourceIdData.getClass()).newInstance(resourceIdData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return out;
	}
	
}
