package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

//id for entity globally (accross different complex resource)
@HAPEntityWithAttribute
public class HAPIdComplexEntityInGlobal extends HAPSerializableImp{

	@HAPAttribute
	public static String ROOTRESOURCEID = "rootResourceId";

	@HAPAttribute
	public static String ENTITYIDINDOMAIN = "entityIdInDomain";

	@HAPAttribute
	public static String PATH = "path";

	//root resource id
	private HAPResourceIdSimple m_rootResourceId;
	
	//entity id within resource domain
	private HAPIdEntityInDomain m_entityIdInDomain;

	//path to entity
	private HAPPath m_path;
	
	public HAPIdComplexEntityInGlobal(HAPResourceIdSimple rootResourceId, HAPPath path, HAPIdEntityInDomain entityIdInDomain) {
		this.m_rootResourceId = rootResourceId;
		this.m_path = path;
		this.m_entityIdInDomain = entityIdInDomain;
	}
	
	public HAPResourceIdSimple getRootResourceId() {   return this.m_rootResourceId;     }
	public HAPIdEntityInDomain getEntityIdInDomain() {    return this.m_entityIdInDomain;    }
	public HAPPath getPath() {   return this.m_path;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ROOTRESOURCEID, this.getRootResourceId().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ENTITYIDINDOMAIN, this.getEntityIdInDomain().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(PATH, this.m_path.getPath());
	}

}
