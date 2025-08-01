package com.nosliw.core.resource;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;

//normalized resource id:
//    root simple resource id
//    path from root
@HAPEntityWithAttribute
public class HAPInfoResourceIdNormalize extends HAPEntityInfoImp{

	@HAPAttribute
	public static final String ROOTRESOURCEID = "rootResourceId";
	
	@HAPAttribute
	public static final String PATH = "path";
	
	@HAPAttribute
	public static final String RESOURCEENTITYTYPE = "resourceEntityType";
	
	//simple resource id
	private HAPResourceIdSimple m_rootResourceId;
	
	private HAPPath m_path;
	
	private HAPIdResourceType m_resourceEntityType;
	
	public HAPInfoResourceIdNormalize(HAPResourceIdSimple rootResourceId, String path, HAPIdResourceType resourceType) {
		this.m_rootResourceId = rootResourceId;
		this.m_path = new HAPPath(path);
		this.m_resourceEntityType = resourceType;
	}
	
	public HAPResourceIdSimple getRootResourceId() {    return this.m_rootResourceId;    }
	
	public HAPPath getPath() {   return this.m_path;    }
	
	public HAPIdResourceType getResourceEntityType() {    return this.m_resourceEntityType;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		HAPUtilityEntityInfo.buildJsonMap(jsonMap, this);
		jsonMap.put(ROOTRESOURCEID, this.m_rootResourceId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATH, this.m_path.getPath());
		jsonMap.put(RESOURCEENTITYTYPE, this.m_resourceEntityType.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof HAPInfoResourceIdNormalize) {
			HAPInfoResourceIdNormalize nResourceId = (HAPInfoResourceIdNormalize)obj;
			if(!HAPUtilityBasic.isEquals(nResourceId.m_path, this.m_path)) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(nResourceId.m_resourceEntityType, this.m_resourceEntityType)) {
				return false;
			}
			if(!HAPUtilityBasic.isEquals(nResourceId.m_rootResourceId, this.m_rootResourceId)) {
				return false;
			}
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return (this.m_rootResourceId.toStringValue(HAPSerializationFormat.LITERATE)+this.m_path.getPath()).hashCode();
	}	
}
