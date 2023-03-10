package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;

@HAPEntityWithAttribute
public class HAPInfoValueStructure extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static final String EXTRAINFO = "extraInfo";

	private HAPDefinitionEntityValueStructure m_valueStructure;
	
	private HAPExtraInfoEntityInDomainDefinition m_extraInfo;
	
	public HAPInfoValueStructure(HAPDefinitionEntityValueStructure valueStructure, HAPExtraInfoEntityInDomainDefinition extraInfo) {
		this.m_valueStructure = valueStructure;
		this.m_extraInfo = extraInfo;
	}
	
	public HAPDefinitionEntityValueStructure getValueStructure() {    return this.m_valueStructure;     }
	
	public HAPExtraInfoEntityInDomainDefinition getExtraInfo() {     return this.m_extraInfo;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructure.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTRAINFO, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
	}

}
