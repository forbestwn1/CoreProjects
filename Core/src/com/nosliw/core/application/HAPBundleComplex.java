package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPBundleComplex extends HAPBundle{

	@HAPAttribute
	public static final String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	public HAPBundleComplex() {
		this.m_valueStructureDomain = new HAPDomainValueStructure();
	}
	
	public HAPDomainValueStructure getValueStructureDomain() {
		return this.m_valueStructureDomain;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toStringValue(HAPSerializationFormat.JSON));
	}	
}
