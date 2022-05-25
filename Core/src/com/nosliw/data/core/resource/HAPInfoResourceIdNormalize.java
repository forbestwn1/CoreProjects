package com.nosliw.data.core.resource;

import com.nosliw.common.path.HAPPath;

//normalized resource id:
//    root simple resource id
//    path from root
public class HAPInfoResourceIdNormalize {

	private HAPResourceId m_rootResourceId;
	
	private HAPPath m_path;
	
	private String m_resourceEntityType;
	
	public HAPInfoResourceIdNormalize(HAPResourceId rootResourceId, String path, String entityType) {
		this.m_rootResourceId = rootResourceId;
		this.m_path = new HAPPath(path);
		this.m_resourceEntityType = entityType;
	}
	
	public HAPResourceId getRootResourceId() {    return this.m_rootResourceId;    }
	public HAPResourceIdSimple getRootResourceIdSimple() {    return (HAPResourceIdSimple)this.m_rootResourceId;    }
	public HAPResourceIdLocal getRootResourceIdLocal() {    return (HAPResourceIdLocal)this.m_rootResourceId;    }
	
	public HAPPath getPath() {   return this.m_path;    }
	
	public String getResourceEntityType() {    return this.m_resourceEntityType;     }
	
}
