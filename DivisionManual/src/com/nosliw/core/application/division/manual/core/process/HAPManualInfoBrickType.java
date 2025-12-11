package com.nosliw.core.application.division.manual.core.process;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPManualInfoBrickType extends HAPSerializableImp{

	@HAPAttribute
	public static String ISCOMPLEX = "isComplex";
	
	//
	private Boolean m_isComplex = null;
	
	public HAPManualInfoBrickType() {
		this.m_isComplex = false;
	}
	
	public HAPManualInfoBrickType(boolean isComplex) {
		this.m_isComplex = isComplex;
	}
	
	public Boolean getIsComplex() {   return this.m_isComplex;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ISCOMPLEX, this.m_isComplex+"");
		typeJsonMap.put(ISCOMPLEX, Boolean.class);
	}
	
}
