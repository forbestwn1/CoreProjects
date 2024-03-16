package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoBrickType extends HAPSerializableImp{

	@HAPAttribute
	public static String ENTITYTYPE = "entityType";

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	//entity type
	private HAPIdBrickType m_entityTypeId;
	
	//
	private Boolean m_isComplex = null;
	
	public HAPInfoBrickType() {
		this.m_entityTypeId = null;
	}
	
	public HAPInfoBrickType(HAPIdBrickType entityTypeId, boolean isComplex) {
		this.m_entityTypeId = entityTypeId;
		this.m_isComplex = isComplex;
	}
	
	public HAPInfoBrickType(HAPIdBrickType entityTypeId) {
		this.m_entityTypeId = entityTypeId;
	}

	public HAPIdBrickType getEntityTypeId() {    return this.m_entityTypeId;    }
	
	public Boolean getIsComplex() {   return this.m_isComplex;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ENTITYTYPE, this.m_entityTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
	}
	
}
