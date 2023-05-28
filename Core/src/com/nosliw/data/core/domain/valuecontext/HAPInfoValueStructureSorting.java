package com.nosliw.data.core.domain.valuecontext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public class HAPInfoValueStructureSorting {

	public static final String VALUESTRUCTURE = "valueStructure";
	
	private HAPWrapperExecutableValueStructure m_valueStructure;
	
	private List<Integer> m_priority;
	
	public HAPInfoValueStructureSorting(HAPWrapperExecutableValueStructure valueStructure) {
		this.m_valueStructure = valueStructure;
	}

	public HAPWrapperExecutableValueStructure getValueStructure() {    return this.m_valueStructure;     }
	
	public List<Integer> getPriority(){   return this.m_priority;    }
	public void setPriority(List<Integer> priority) {     this.m_priority = priority;     }

	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructure.toExpandedString(valueStructureDomain));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
}
