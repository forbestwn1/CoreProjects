package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPInfoBrickType extends HAPSerializableImp{

	@HAPAttribute
	public static String BRICKTYPE = "brickType";

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	//entity type
	private HAPIdBrickType m_brickTypeId;
	
	//
	private Boolean m_isComplex = null;
	
	public HAPInfoBrickType() {
		this.m_brickTypeId = null;
	}
	
	public HAPInfoBrickType(HAPIdBrickType brickTypeId, boolean isComplex) {
		this.m_brickTypeId = brickTypeId;
		this.m_isComplex = isComplex;
	}
	
	public HAPInfoBrickType(HAPIdBrickType brickTypeId) {
		this.m_brickTypeId = brickTypeId;
	}

	public HAPIdBrickType getBrickTypeId() {    return this.m_brickTypeId;    }
	
	public Boolean getIsComplex() {   return this.m_isComplex;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(BRICKTYPE, this.m_brickTypeId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
	}
	
}
