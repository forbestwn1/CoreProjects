package com.nosliw.data.core.resource;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;

//normalized resource id:
//    root simple resource id
//    path from root
public class HAPInfoResourceIdNormalize extends HAPEntityInfoImp{

	public static final String ROOTRESOURCEID = "rootResourceId";
	
	public static final String PATH = "path";
	
	public static final String RESOURCEENTITYTYPE = "resourceEntityType";
	
	//it maybe simple or local resource id
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
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		HAPUtilityEntityInfo.buildJsonMap(jsonMap, this);
		jsonMap.put(ROOTRESOURCEID, this.m_rootResourceId.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(PATH, this.m_path.getPath());
		jsonMap.put(RESOURCEENTITYTYPE, this.m_resourceEntityType);
	}

}
