package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;

@HAPEntityWithAttribute
public class HAPInfoValueStructureDefinition extends HAPSerializableImp{

	@HAPAttribute
	public static final String VALUESTRUCTURE = "valueStructure";

	@HAPAttribute
	public static final String EXTRAINFO = "extraInfo";

	private HAPManualBrickValueStructure m_valueStructure;
	
	private HAPExtraInfoEntityInDomainDefinition m_extraInfo;
	
	public HAPInfoValueStructureDefinition(HAPManualBrickValueStructure valueStructure) {
		this.m_valueStructure = valueStructure;
	}
	
	public HAPManualBrickValueStructure getValueStructure() {    return this.m_valueStructure;     }
	
	public HAPExtraInfoEntityInDomainDefinition getExtraInfo() {     return this.m_extraInfo;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructure.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_extraInfo!=null) jsonMap.put(EXTRAINFO, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
	}

}
