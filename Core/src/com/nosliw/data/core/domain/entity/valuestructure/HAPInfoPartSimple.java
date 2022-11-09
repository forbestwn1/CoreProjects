package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

public class HAPInfoPartSimple {

	public static final String PART = "part";
	
	private HAPExecutablePartComplexValueStructureSimple m_simpleStructurePart;
	
	private List<Integer> m_priority;
	
	public HAPInfoPartSimple(HAPExecutablePartComplexValueStructureSimple simpleStructurePart) {
		this.m_simpleStructurePart = simpleStructurePart;
	}

	public HAPExecutablePartComplexValueStructureSimple getSimpleValueStructurePart() {	return this.m_simpleStructurePart;	}
	
	public List<Integer> getPriority(){   return this.m_priority;    }
	public void setPriority(List<Integer> priority) {     this.m_priority = priority;     }

	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(PART, this.m_simpleStructurePart.toExpandedString(valueStructureDomain));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
}
