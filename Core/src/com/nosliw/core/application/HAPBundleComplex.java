package com.nosliw.core.application;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPBundleComplex extends HAPBundle{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";
	
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
		Map<String, String> attrJsonMap = new LinkedHashMap<String, String>();
		
		
		for(HAPAttributeInBrick attr : this.m_attributes) {
			attrJsonMap.put(attr.getName(), attr.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ATTRIBUTE, HAPUtilityJson.buildMapJson(attrJsonMap));
		jsonMap.put(BRICKTYPEID, this.m_brickTypeId.toStringValue(HAPSerializationFormat.JSON));
	}
	
}
