package com.nosliw.data.core.domain.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPInfoValueType extends HAPSerializableImp{

	@HAPAttribute
	public static String VALUETYPE = "valueType";

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	private String m_valueType;
	
	private boolean m_isComplex = false;
	
	public HAPInfoValueType() {
		this.m_valueType = null;
		this.m_isComplex = false;
	}
	
	public HAPInfoValueType(String valueType, boolean isComplex) {
		this.m_valueType = valueType;
		this.m_isComplex = isComplex;
	}
	
	public String getValueType() {    return this.m_valueType;    }
	
	public boolean getIsComplex() {   return this.m_isComplex;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(VALUETYPE, this.m_valueType);
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
	}
	
}
