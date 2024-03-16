package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPIdBrickType extends HAPSerializableImp{

	public static final String ENTITYTYPE = "entityType";
	
	public static final String VERSION = "version";
	
	//type of entity
	private String m_entityType;
	
	//version, what entity executable looks like
	private String m_version;

	public HAPIdBrickType() {}
	
	public HAPIdBrickType(String entityType, String version) {
		this.m_entityType = entityType;
		this.m_version = version;
	}
	
	public HAPIdBrickType(String key) {
		String[] segs = HAPUtilityNamingConversion.parseLevel1(key);
		this.m_entityType = segs[0];
		if(segs.length>1) {
			this.m_version = segs[1];
		}
	}
	
	public String getEntityType() {    return this.m_entityType;    }
	
	public String getVersion() {   return this.m_version;     }
	public void setVersion(String version) {   this.m_version = version;     }

	public String getKey() {
		return HAPUtilityNamingConversion.cascadeLevel1(this.m_entityType, this.m_version);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYTYPE, this.m_entityType);
		jsonMap.put(VERSION, this.m_version);
	}

}
