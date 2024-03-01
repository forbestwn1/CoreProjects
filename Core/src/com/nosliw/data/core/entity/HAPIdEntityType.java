package com.nosliw.data.core.entity;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPIdEntityType extends HAPSerializableImp{

	//type of entity
	private String m_entityType;
	
	//version, what entity executable looks like
	private String m_version;

	public HAPIdEntityType() {}
	
	public HAPIdEntityType(String entityType, String version) {
		this.m_entityType = entityType;
		this.m_version = version;
	}
	
	public HAPIdEntityType(String key) {
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
}
