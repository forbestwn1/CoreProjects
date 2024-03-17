package com.nosliw.core.application.valuecontext;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public class HAPInfoPartSimple {

	public static final String PART = "part";
	
	private HAPPartInValueContextSimple m_simpleStructurePart;
	
	private List<Integer> m_priority;
	
	public HAPInfoPartSimple(HAPPartInValueContextSimple simpleStructurePart) {
		this.m_simpleStructurePart = simpleStructurePart;
	}

	public HAPPartInValueContextSimple getSimpleValueStructurePart() {	return this.m_simpleStructurePart;	}
	
	public List<Integer> getPriority(){   return this.m_priority;    }
	public void setPriority(List<Integer> priority) {     this.m_priority = priority;     }

	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(PART, this.m_simpleStructurePart.toExpandedString(valueStructureDomain));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
}
