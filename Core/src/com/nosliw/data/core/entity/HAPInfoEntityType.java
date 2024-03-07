package com.nosliw.data.core.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoEntityType extends HAPSerializableImp{

	@HAPAttribute
	public static String ENTITYTYPE = "entityType";

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	//entity type
	private HAPIdEntityType m_entityTypeId;
	
	//
	private Boolean m_isComplex = null;
	
	public HAPInfoEntityType() {
		this.m_entityTypeId = null;
	}
	
	public HAPInfoEntityType(HAPIdEntityType entityTypeId, boolean isComplex) {
		this.m_entityTypeId = entityTypeId;
		this.m_isComplex = isComplex;
	}
	
	public HAPInfoEntityType(HAPIdEntityType entityTypeId) {
		this.m_entityTypeId = entityTypeId;
	}

	public HAPIdEntityType getEntityTypeId() {    return this.m_entityTypeId;    }
	
	public Boolean getIsComplex(HAPManagerEntity entityMan) {
		if(this.m_isComplex==null) {
			this.m_isComplex = entityMan.getEntityTypeInfo(m_entityTypeId).getIsComplex(entityMan);
		}
		return this.m_isComplex;    
	}

	public Boolean getIsComplex() {   return this.m_isComplex;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ENTITYTYPE, this.m_entityTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
	}
	
}
